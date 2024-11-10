import React, { useState, useEffect } from 'react';
import { Form, Button, Input, Select, Spin, message, Popover } from 'antd';
import api from '../../axious/axious';
import { ref, getDownloadURL } from 'firebase/storage';
import { storage } from '../../firebase/firebase';
import koiFishLogo from '../../assets/images/koifish_logo.svg';
import pondLogo from '../../assets/images/pond_logo.svg';
import descriptionLogo from '../../assets/images/description_logo.svg';
import noteLogo from '../../assets/images/note_logo.svg';

import '../../css/ConsultationRequestDetail.css';

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
    document.body.classList.add('khoi_body');

  // Gỡ bỏ lớp `khoi_body` khi component bị unmount
  return () => {
    document.body.classList.remove('khoi_body');
  };
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
        animalCategoryIds: selectedAnimal,
        shelterCategoryIds: selectedShelter,
        description,
      };
      await api.post(`/api/consultation-request-details/request-id/${requestId}`, payload);
      localStorage.setItem('consultationSuccess', 'true');
      window.location.href = '/';
    } catch (error) {
      message.error('Error saving consultation request details.');
    }
  };  

  return (
    <div className="khoi_con_req_de_consultation-request-detail-container">
      <h1>Consultation Request Details</h1>
      {loading ? (
        <Spin />
      ) : (
        <Form onFinish={handleFormSubmit}>
          {(packageId === '1' || packageId === '3') && (
            <Form.Item
              label={<span><img src={koiFishLogo} alt="Fish Icon" className="khoi_con_req_de_form-icon" /> Select Fish</span>}
              name="selectedAnimal"
              rules={[{ required: true, message: 'Please select at least one fish.' }]}
              className="khoi_con_req_de_form-item"
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
                        <div className="khoi_con_req_de_popover-content">
                          <div className="khoi_con_req_de_popover-image-container">
                            {animal.imageUrl ? (
                              <img
                                src={animal.imageUrl}
                                alt={animal.animalCategoryName}
                                className="khoi_con_req_de_animal-image"
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
              label={<span><img src={pondLogo} alt="Pond Icon" className="khoi_con_req_de_form-icon" /> Select Pond</span>}
              name="selectedShelter"
              rules={[{ required: true, message: 'Please select at least one pond.' }]}
              className="khoi_con_req_de_form-item"
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
                        <div className="khoi_con_req_de_popover-content">
                          <div className="khoi_con_req_de_popover-image-container">
                            <span>{shelter.shelterCategoryName}</span>
                          </div>
                          <div>
                            <p><strong>Name:</strong> {shelter.shelterCategoryName}</p>
                            <p><strong>Description:</strong> {shelter.description}</p>
                            <p><strong>Dimensions:</strong> {`${shelter.width} x ${shelter.height} x ${shelter.length}`}</p>
                            <p><strong>Water Volume:</strong> {shelter.waterVolume}</p>
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
            label={<span><img src={descriptionLogo} alt="Description Icon" className="khoi_con_req_de_form-icon" /> Detailed Description</span>}
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
            className="khoi_con_req_de_form-item"
          >
            <Input.TextArea
              rows={4}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              autoSize={{ minRows: 5, maxRows: 10 }}
              className="khoi_con_req_de_textarea"
            />
          </Form.Item>

          <Button type="primary" htmlType="submit" className="khoi_con_req_de_submit-button">
            Save Details
          </Button>
        </Form>
      )}

      <div className="khoi_con_req_de_note-section">
        <img src={noteLogo} alt="Note Icon" className="khoi_con_req_de_note-icon" />
        <p>
          Please provide specific details, such as the number of fish with their colors, or detailed pond information you have, so our consultation can be more effective.
        </p>
      </div>
    </div>
  );
};

export default ConsultationRequestDetail;
