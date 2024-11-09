import React, { useState, useEffect } from 'react';
import { Form, Button, Input, Select, Spin, message, Popover } from 'antd';
import api from '../../axious/axious';
import { ref, getDownloadURL } from 'firebase/storage';
import { storage } from '../../firebase/firebase';

const { Option } = Select;

const ConsultationRequestDetail: React.FC = () => {
  const requestId = localStorage.getItem('requestId');
  const packageId = localStorage.getItem('selectedPackageId');

  const [loading, setLoading] = useState<boolean>(true);
  const [animalCategories, setAnimalCategories] = useState<any[]>([]);
  const [shelterCategories, setShelterCategories] = useState<any[]>([]);
  const [selectedAnimal, setSelectedAnimal] = useState<number[]>([]);
  const [selectedShelter, setSelectedShelter] = useState<number[]>([]);
  const [description, setDescription] = useState<string>('');
  const [hoveredAnimal, setHoveredAnimal] = useState<any | null>(null);
  const [hoveredShelter, setHoveredShelter] = useState<any | null>(null);
  const [shapeDetails, setShapeDetails] = useState<any | null>(null);

  useEffect(() => {
    if (!requestId || !packageId) {
      message.error('Missing required information (requestId or packageId)!');
      return;
    }
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    setLoading(true);
    try {
      const animalResponse = await api.get('/animals/animalCategory');
      const shelterResponse = await api.get('/shelters/shelterCategory');
      if (animalResponse.data.code === 1000 && Array.isArray(animalResponse.data.result)) {
        const animalDataWithImages = await Promise.all(
          animalResponse.data.result.map(async (animal: any) => {
            try {
              if (animal.imageUrl) {
                const imagePath = decodeURIComponent(animal.imageUrl.split('/o/')[1].split('?')[0]);
                animal.imageUrl = await getDownloadURL(ref(storage, imagePath));
              }
            } catch {
              animal.imageUrl = null;
            }
            return animal;
          })
        );
        setAnimalCategories(animalDataWithImages);
      }
      if (shelterResponse.data.code === 1000 && Array.isArray(shelterResponse.data.result)) {
        setShelterCategories(shelterResponse.data.result);
      }
    } catch (error) {
      message.error('Error loading categories.');
    } finally {
      setLoading(false);
    }
  };

  const fetchShapeDetails = async (shapeId: number) => {
    try {
      const response = await api.get(`/shapes/${shapeId}`);
      if (response.data.code === 1000) {
        setShapeDetails(response.data.result);
      }
    } catch (error) {
      message.error('Error loading shape details.');
    }
  };

  const handleFormSubmit = async () => {
    if (!selectedAnimal.length && (packageId === '1' || packageId === '3')) {
      message.error('Please select at least one fish.');
      return;
    }
if (!selectedShelter.length && (packageId === '2' || packageId === '3')) {
      message.error('Please select at least one pond.');
      return;
    }
    if (description.split(' ').filter(Boolean).length < 100) {
      message.error('The detailed description must be at least 100 words.');
      return;
    }
  
    try {
      const payload = {
        animalCategoryIds: selectedAnimal, // Contains selected animal IDs
        shelterCategoryIds: selectedShelter, // Contains selected shelter IDs
        description,
      };
      await api.post(`/api/consultation-request-details/request-id/${requestId}`, payload);
      
      // Save success status in localStorage before navigating away
      localStorage.setItem('consultationSuccess', 'true');
      window.location.href = '/'; // Redirect to home page
      
    } catch (error) {
      message.error('Error saving consultation request details.');
    }
  };  

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px' }}>
      <h1>Consultation Request Details</h1>
      {loading ? (
        <Spin />
      ) : (
        <Form onFinish={handleFormSubmit}>
          {(packageId === '1' || packageId === '3') && (
            <Form.Item
              label="Select Fish"
              name="selectedAnimal"
              rules={[{ required: true, message: 'Please select at least one fish.' }]}
            >
              <Select
                mode="multiple"
                placeholder="Select fish"
                value={selectedAnimal}
                onChange={(values) => setSelectedAnimal(values)}
              >
                {animalCategories.map((animal) => (
                  <Option key={animal.id} value={animal.id}>
                    <Popover
                      content={
                        <div style={{ display: 'flex', alignItems: 'flex-start', maxWidth: '350px' }}>
                          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', width: '150px', height: '150px', marginRight: '10px', border: '1px solid #ccc' }}>
                            {animal.imageUrl ? (
                              <img
                                src={animal.imageUrl}
                                alt={animal.animalCategoryName}
                                style={{ maxWidth: '100%', maxHeight: '100%', objectFit: 'contain' }}
                              />
                            ) : (
                              <span>{animal.animalCategoryName}</span>
                            )}
                          </div>
                          <div>
                            <p><strong>Name:</strong> {animal.animalCategoryName}</p>
                            <p><strong>Description:</strong> {animal.description}</p>
                            <p><strong>Origin:</strong> {animal.origin}</p>
                            <p><strong>Status:</strong> {animal.status}</p>
<p><strong>Creation Date:</strong> {new Date(animal.createdDate).toLocaleDateString()}</p>
                            <p><strong>Created By:</strong> {animal.createdBy}</p>
                            {animal.colors && animal.colors.length > 0 && (
                              <p><strong>Colors:</strong> {animal.colors.map((color: any) => color.color).join(', ')}</p>
                            )}
                          </div>
                        </div>
                      }
                      title="Fish Details"
                      trigger="hover"
                      placement="right"
                      onVisibleChange={(visible) => visible && setHoveredAnimal(animal)}
                    >
                      {animal.animalCategoryName}
                    </Popover>
                  </Option>
                ))}
              </Select>
            </Form.Item>
          )}

          {(packageId === '2' || packageId === '3') && (
            <Form.Item
              label="Select Pond"
              name="selectedShelter"
              rules={[{ required: true, message: 'Please select at least one pond.' }]}
            >
              <Select
                mode="multiple"
                placeholder="Select pond"
                value={selectedShelter}
                onChange={(values) => setSelectedShelter(values)}
              >
                {shelterCategories.map((shelter) => (
                  <Option key={shelter.id} value={shelter.id}>
                    <Popover
                      content={
                        <div style={{ display: 'flex', alignItems: 'flex-start', maxWidth: '350px' }}>
                          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', width: '150px', height: '150px', marginRight: '10px', border: '1px solid #ccc' }}>
                            <span>{shelter.shelterCategoryName}</span>
                          </div>
                          <div>
                            <p><strong>Name:</strong> {shelter.shelterCategoryName}</p>
                            <p><strong>Description:</strong> {shelter.description}</p>
                            <p><strong>Dimensions:</strong> {`${shelter.width} x ${shelter.height} x ${shelter.length}`}</p>
                            <p><strong>Diameter:</strong> {shelter.diameter}</p>
                            <p><strong>Water Volume:</strong> {shelter.waterVolume}</p>
                            <p><strong>Water Filtration System:</strong> {shelter.waterFiltrationSystem}</p>
                            <p><strong>Shape:</strong> {shapeDetails?.shape}</p>
                          </div>
                        </div>
                      }
                      title="Pond Details"
                      trigger="hover"
                      placement="right"
                      onVisibleChange={(visible) => visible && fetchShapeDetails(shelter.shape?.id)}
>
                      {shelter.shelterCategoryName}
                    </Popover>
                  </Option>
                ))}
              </Select>
            </Form.Item>
          )}

          <Form.Item
            label="Detailed Description"
            name="description"
            rules={[
              { required: true, message: 'Detailed description is required.' },
              {
                validator: (_, value) => {
                  if (value) {
                    const wordCount = value.trim().split(/\s+/).filter((word: string) => word.length > 0).length;
                    if (wordCount < 100) {
                      return Promise.reject(new Error('The detailed description must be at least 100 words.'));
                    }
                  }
                  return Promise.resolve();
                },
              },
            ]}
          >
            <Input.TextArea
              rows={4}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </Form.Item>

          <Button type="primary" htmlType="submit">
            Save Details
          </Button>
        </Form>
      )}
    </div>
  );
};

export default ConsultationRequestDetail;