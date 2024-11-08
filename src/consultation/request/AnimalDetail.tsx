import React, { useEffect, useState } from 'react';
import { Carousel, Spin } from 'antd';
import { useParams } from 'react-router-dom'; // Import useParams
import api from '../../axious/axious'; // Adjust path as necessary
import { ref, getDownloadURL } from 'firebase/storage';
import { storage } from '../../firebase/firebase';

interface AnimalData {
  animal_category_id: number;
  animal_category_name: string;
  created_date: string;
  description: string;
  origin: string;
  status: string;
  animalImages: { imageUrl: string }[]; // Assuming this structure based on your use case
}

const AnimalDetail: React.FC = () => {
  const { animalId } = useParams<{ animalId: string }>(); // Use useParams to get animalId
  const [animal, setAnimal] = useState<AnimalData | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAnimalDetails = async () => {
      if (!animalId) return;

      try {
        const response = await api.get(`/animals/${animalId}`);
        if (response.data.code === 1000) {
          const animalData = response.data.result;

          // Load image URLs from Firebase
          const imagesWithUrl = await Promise.all(
            animalData.animalImages.map(async (image: { imageUrl: string }) => {
              try {
                const imagePath = decodeURIComponent(image.imageUrl.split('/o/')[1].split('?')[0]);
                const url = await getDownloadURL(ref(storage, imagePath));
                return { ...image, imageUrl: url };
              } catch {
                return null; // Ignore images that fail to load
              }
            })
          );
          animalData.animalImages = imagesWithUrl.filter((img: any) => img !== null); // Remove null entries
          setAnimal(animalData);
        }
      } catch (error) {
        console.error('Failed to fetch animal details:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchAnimalDetails();
  }, [animalId]);

  if (loading) return <Spin />;
  if (!animal) return <div>No details available for this animal.</div>;

  return (
    <div>
      <h2>{animal.animal_category_name}</h2>
      <Carousel autoplay>
        {animal.animalImages.length > 0 ? (
          animal.animalImages.map((img, index) => (
            <div key={index}>
              <img
                src={img.imageUrl}
                alt={`Image of ${animal.animal_category_name}`}
                style={{ maxWidth: '100%', height: 'auto', objectFit: 'contain' }}
              />
            </div>
          ))
        ) : (
          <div>No images available</div>
        )}
      </Carousel>
      <p><strong>Description:</strong> {animal.description}</p>
      <p><strong>Origin:</strong> {animal.origin}</p>
      <p><strong>Created Date:</strong> {animal.created_date}</p>
      <p><strong>Status:</strong> {animal.status}</p>
    </div>
  );
};

export default AnimalDetail;
