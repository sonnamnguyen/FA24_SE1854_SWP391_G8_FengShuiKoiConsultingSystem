import React, { useEffect, useState } from 'react';
import { Carousel, Spin } from 'antd';
import { useParams } from 'react-router-dom'; // Import useParams
import api from '../../axious/axious';
import { ref, getDownloadURL } from 'firebase/storage';
import { storage } from '../../firebase/firebase';

interface ShelterData {
  shelter_category_id: number;
  shelter_category_name: string;
  created_date: string;
  description: string;
  diameter: number;
  height: number;
  length: number;
  width: number;
  water_volume: number;
  water_filtration_system: string;
  status: string;
  shelterImages: { imageUrl: string }[];
}

const ShelterDetail: React.FC = () => {
  const { shelterId } = useParams<{ shelterId: string }>(); // Use useParams to get shelterId
  const [shelter, setShelter] = useState<ShelterData | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchShelterDetails = async () => {
      if (!shelterId) return;

      try {
        const response = await api.get(`/shelters/${shelterId}`);
        if (response.data.code === 1000) {
          const shelterData = response.data.result;

          // Load image URLs from Firebase
          const imagesWithUrl = await Promise.all(
            shelterData.shelterImages.map(async (image: { imageUrl: string }) => {
              try {
                const imagePath = decodeURIComponent(image.imageUrl.split('/o/')[1].split('?')[0]);
                const url = await getDownloadURL(ref(storage, imagePath));
                return { ...image, imageUrl: url };
              } catch {
                return null;
              }
            })
          );
          shelterData.shelterImages = imagesWithUrl.filter((img: any) => img !== null); // Remove null entries
          setShelter(shelterData);
        }
      } catch (error) {
        console.error('Failed to fetch shelter details:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchShelterDetails();
  }, [shelterId]);

  if (loading) return <Spin />;
  if (!shelter) return <div>No details available for this shelter.</div>;

  return (
    <div>
      <h2>{shelter.shelter_category_name}</h2>
      <Carousel autoplay>
        {shelter.shelterImages.length > 0 ? (
          shelter.shelterImages.map((img, index) => (
            <div key={index}>
              <img
                src={img.imageUrl}
                alt={`Image of ${shelter.shelter_category_name}`}
                style={{ maxWidth: '100%', height: 'auto', objectFit: 'contain' }}
              />
            </div>
          ))
        ) : (
          <div>No images available</div>
        )}
      </Carousel>
      <p><strong>Description:</strong> {shelter.description}</p>
      <p><strong>Dimensions:</strong> {`${shelter.width} x ${shelter.height} x ${shelter.length}`}</p>
      <p><strong>Diameter:</strong> {shelter.diameter}</p>
      <p><strong>Water Volume:</strong> {shelter.water_volume}</p>
      <p><strong>Water Filtration System:</strong> {shelter.water_filtration_system}</p>
      <p><strong>Created Date:</strong> {shelter.created_date}</p>
      <p><strong>Status:</strong> {shelter.status}</p>
    </div>
  );
};

export default ShelterDetail;
