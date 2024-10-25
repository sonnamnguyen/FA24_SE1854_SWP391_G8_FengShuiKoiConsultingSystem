import React, { ChangeEvent, useEffect, useState } from "react";
import AnimalCategory from "../models/AnimalCategory";
import Pagination from "../utils/Pagination";
import { Button, Form, Input, Popconfirm, Modal, Upload, Checkbox, Divider, Carousel, notification } from 'antd';
import { Table, Tag, Space } from 'antd';
import { Radio } from 'antd';

import { SearchOutlined, PlusOutlined } from '@ant-design/icons';
import api from "../axious/axious";
import { CheckboxProps } from 'antd';
import { ref, uploadString, getDownloadURL } from "firebase/storage";
import { storage } from "../firebase/firebase";
import Color from "../models/Color";
import ShelterCategory from "../models/ShelterCategory";
import { findByShelterCategory, getAllShelters } from "./api/ShelterCategoryAPI";
import Shape from "../models/Shape";

interface Shapes {
  id: number;
  shape: string;
}

const ShelterCollection: React.FC = () => {
  const [listShelterCategory, setListShelterCategory] = useState<ShelterCategory[]>([]);
  const [reloadData, setReloadData] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [pageNow, setPageNow] = useState(1);
  const [totalPage, setTotalPage] = useState(0);
  const [name, setName] = useState("");
  const [searchReload, setSearchReload] = useState("");
  const [plainOptions, setPlainOptions] = useState<Shapes[]>([]);
  const [checkedList, setCheckedList] = useState<number[]>([]);
  const [shelterImages, setShelterImages] = useState<File[]>([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isUpdateMode, setIsUpdateMode] = useState(false);
  const [selectedShelter, setSelectedShelter] = useState<ShelterCategory | null>(null);
  const [selectedShape, setSelectedShape] = useState<number | null>(null);

  const pageSize = 10;

  const [apii, contextHolder] = notification.useNotification();

  const reloadShelterList = () => {
    setReloadData(true);

    if (name === "") {
      getAllShelters()
        .then((shelterData) => {
          if (shelterData) {
            setListShelterCategory(shelterData.result);
            setTotalPage(shelterData.pageTotal);
          } else {
            setError("No data found.");
          }
          setReloadData(false);
        })
        .catch((error) => {
          setError(error.message);
          setReloadData(false);
        });
    } else {
      findByShelterCategory(name)
        .then((shelterData) => {
          if (shelterData) {
            setListShelterCategory(shelterData.result);
            setTotalPage(shelterData.pageTotal);
          } else {
            setError("No data found.");
          }
          setReloadData(false);
        })
        .catch((error) => {
          setError(error.message);
          setReloadData(false);
        });
    }
  };

  useEffect(() => {
    const fetchShapes = async () => {
      const colors = await getAllShapes();
      setPlainOptions(colors || []);
    };
    fetchShapes();
    reloadShelterList();
  }, [pageNow, name]);




  const getAllShapes = async (): Promise<Shapes[] | null> => {
    try {
      const response = await api.get('/shapes/getAll-Shapes');
      return response.data.code === 1000 ? response.data.result.map((shape: any) => ({ id: shape.id, shape: shape.shape })) : null;
    } catch (error) {
      console.error("Error fetching shapes: ", error);
      return null;
    }
  };

  const handleView = (id: number) => {
    if (id === undefined) {
      console.error("ID is undefined, cannot delete the shelter.");
      return;
    }
    const shelter = listShelterCategory.find(shelter => shelter.id === id);
    if (shelter) {
      setSelectedShelter(shelter);
      setIsModalVisible(true);
      setIsUpdateMode(false);
    }
  };

  const handleUpdate = (id: number) => {
    const shelter = listShelterCategory.find(shelter => shelter.id === id);
    if (shelter) {
      setSelectedShelter(shelter);
      setIsModalVisible(true);
      setIsUpdateMode(true);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      const response = await api.delete(`/shelters/${id}`);
      if (response.data.code === 1000) {
        apii.success({ message: 'Success', description: 'Shelter has been successfully deleted.' });
        reloadShelterList();
      } else {
        apii.error({ message: 'Error', description: 'Failed to delete shelter.' });
      }
    } catch (error) {
      apii.error({ message: 'Error', description: 'Error deleting shelter.' });
    }
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
  };

  const uploadImagesToFirebase = async (files: File[]): Promise<string[]> => {
    const uploadPromises = files.map(async (file) => {
      const storageRef = ref(storage, `shelter_images/${file.name}`);
      const base64Image = await getBase64(file);
      await uploadString(storageRef, base64Image, 'data_url');
      return await getDownloadURL(storageRef);
    });
    return Promise.all(uploadPromises);
  };

  const getBase64 = (file: File): Promise<string> => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = (error) => reject(error);
    });
  };

  const handleUploadChange = (info: any) => {
    setShelterImages(info.fileList.map((file: any) => file.originFileObj));
  };

  const handleSubmit = async () => {
    const base64Avatars = await uploadImagesToFirebase(shelterImages);
    try {
      const response = await api.put(`/animals/${selectedShelter?.id}`, {
        shelterCategoryName: selectedShelter?.shelterCategoryName,
        description: selectedShelter?.description,
        shape: { id: selectedShape },
        width: selectedShelter?.width,
        height: selectedShelter?.height,
        length: selectedShelter?.length,
        diameter: selectedShelter?.diameter,
        waterVolume: selectedShelter?.waterVolume,
        waterFiltrationSystem: selectedShelter?.waterFiltrationSystem,
        shelterImages: base64Avatars.map(url => ({ imageUrl: url })),
      });

      if (response.data.code === 1000) {
        apii.success({ message: 'Success', description: 'Shelter has been successfully updated.' });
        setIsModalVisible(false);
        reloadShelterList();
      } else {
        apii.error({ message: 'Error', description: 'Failed to update shelter.' });
      }
    } catch (error) {
      apii.error({ message: 'Error', description: 'Error updating shelter.' });
    }
  };

  const pagination = (page: number) => {
    setPageNow(page);
  };

  const handleSearch = () => {
    setName(searchReload);
    setReloadData(true);
    setPageNow(1);
  };
  const columns = [
    {
      title: 'STT',
      dataIndex: 'index',
      key: 'index',
      render: (text: string, record: ShelterCategory, index: number) => index + 1 + (pageNow - 1) * pageSize,
    },
    {
      title: 'Image',
      dataIndex: 'shelterImages',
      key: 'shelterImages',
      render: (images: any) => (
        <img
          src={images && images.length > 0 ? images[0].imageUrl : "path_to_placeholder.jpg"}
          alt="Koi"
          style={{ width: "100px", height: "100px", objectFit: "cover" }}
        />
      ),
    },
    {
      title: 'Ponds Name',
      dataIndex: 'shelterCategoryName',
      key: 'shelterCategoryName',
      render: (text: string) => (
        <span
          title={text}
          style={{ display: 'block', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', maxWidth: '150px' }}
        >
          {text}
        </span>
      ),
    },
    {
      title: 'Action',
      key: 'action',
      render: (text: string, record: ShelterCategory) => (
        <Space size="middle">
          <Button
            type="primary"
            onClick={() => {
              if (record.id !== undefined) {
                handleView(record.id);
              } else {
                console.error("Shelter ID is undefined, cannot view.");
              }
            }}
          >
            View
          </Button>
          <Button
            onClick={() => {
              if (record.id !== undefined) {
                handleUpdate(record.id);
              } else {
                console.error("Shelter ID is undefined, cannot update.");
              }
            }}
          >
            Update
          </Button>
          <Popconfirm
            title="Delete the Pond"
            okText="Yes"
            cancelText="No"
            onConfirm={() => {
              if (record.id !== undefined) {
                handleDelete(record.id);
              } else {
                console.error("Shelter ID is undefined, cannot delete.");
              }
            }}
          >
            <Button type="primary" danger>
              Delete
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const onSearchInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchReload(e.target.value);
  };

  return (
    <>
      {contextHolder}
      <div className="d-flex justify-content-between mb-3">
        <div className="d-flex">
          <Input
            value={searchReload}
            onChange={onSearchInputChange}
            className="mr-2"
            placeholder="Search by name"
          />
          <Button type="primary" icon={<SearchOutlined />} onClick={handleSearch}>
            Search
          </Button>
        </div>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => setIsModalVisible(true)}>
          Add Pond
        </Button>
      </div>
      <Table
        columns={columns}
        dataSource={listShelterCategory}
        pagination={false}
        rowKey="id"
      />
      <Pagination
        currentPage={pageNow}
        totalPages={totalPage}
        pagination={pagination}
      />
      <Modal
        title={isUpdateMode ? "Update Shelter Details" : "Shelter Details"}
        open={isModalVisible}
        onOk={isUpdateMode ? handleSubmit : handleModalCancel}
        onCancel={handleModalCancel}
        width={1000}
        style={{ top: '15%' }}
      >
        {isUpdateMode ? (
          <Form onFinish={handleSubmit}>
            <Form.Item
              label="Pond Name"
              required
              rules={[{ required: true, message: 'Please input the pond name!' }]}
            >
              <Input
                value={selectedShelter?.shelterCategoryName}
                onChange={(e) => setSelectedShelter((prev) => prev && { ...prev, shelterCategoryName: e.target.value })}
              />
            </Form.Item>

            <Form.Item
              label="Width"
              required
              rules={[{ required: true, message: 'Please input the width!' }]}
            >
              <Input
                type="number"
                value={selectedShelter?.width}
                onChange={(e) => setSelectedShelter((prev) => prev && { ...prev, width: Number(e.target.value) })}
              />
            </Form.Item>

            <Form.Item
              label="Height"
              required
              rules={[{ required: true, message: 'Please input the height!' }]}
            >
              <Input
                type="number"
                value={selectedShelter?.height}
                onChange={(e) => setSelectedShelter((prev) => prev && { ...prev, height: Number(e.target.value) })}
              />
            </Form.Item>

            <Form.Item
              label="Length"
              required
              rules={[{ required: true, message: 'Please input the length!' }]}
            >
              <Input
                type="number"
                value={selectedShelter?.length}
                onChange={(e) => setSelectedShelter((prev) => prev && { ...prev, length: Number(e.target.value) })}
              />
            </Form.Item>

            <Form.Item
              label="Diameter"
              required
              rules={[{ required: true, message: 'Please input the diameter!' }]}
            >
              <Input
                type="number"
                value={selectedShelter?.diameter}
                onChange={(e) => setSelectedShelter((prev) => prev && { ...prev, diameter: Number(e.target.value) })}
              />
            </Form.Item>

            <Form.Item
              label="Water Volume"
              required
              rules={[{ required: true, message: 'Please input the water volume!' }]}
            >
              <Input
                type="number"
                value={selectedShelter?.waterVolume}
                onChange={(e) => setSelectedShelter((prev) => prev && { ...prev, waterVolume: Number(e.target.value) })}
              />
            </Form.Item>

            <Form.Item
              label="Water Filtration System"
              required
              rules={[{ required: true, message: 'Please input the water filtration system!' }]}
            >
              <Input
                value={selectedShelter?.waterFiltrationSystem}
                onChange={(e) => setSelectedShelter((prev) => prev && { ...prev, waterFiltrationSystem: e.target.value })}
              />
            </Form.Item>

            <Form.Item
              label="Description"
              required
              rules={[{ required: true, message: 'Please input the description!' }]}
            >
              <Input.TextArea
                value={selectedShelter?.description}
                onChange={(e) => setSelectedShelter((prev) => prev && { ...prev, description: e.target.value })}
              />
            </Form.Item>

            <Form.Item label="Images">
              <Upload
                multiple
                accept="image/*"
                showUploadList={true}
                beforeUpload={() => false}
                listType="picture-card"
                onChange={handleUploadChange}
              >
                <div>
                  <PlusOutlined />
                  <div style={{ marginTop: 8 }}>Upload</div>
                </div>
              </Upload>
            </Form.Item>

            <Form.Item label="Shape">
              <Radio.Group
                onChange={e => setSelectedShape(e.target.value)}
                value={selectedShape || selectedShelter?.shape?.id}
              >
                {plainOptions.map(shape => (
                  <Radio key={shape.id} value={shape.id}>
                    {shape.shape}
                  </Radio>
                ))}
              </Radio.Group>
            </Form.Item>



            <Form.Item>
              <Button type="primary" htmlType="submit">
                Submit
              </Button>
            </Form.Item>
          </Form>
        ) : (
          <div style={{ display: 'flex' }}>
            <div style={{ flex: 1 }}> {/* Phần dành cho Carousel */}
              <Carousel autoplay>
                {selectedShelter?.shelterImages?.length ? (
                  selectedShelter.shelterImages.map((image, index) => (
                    <div key={index}>
                      <img
                        src={image.imageUrl}
                        alt={`Image of ${selectedShelter?.shelterCategoryName}`}
                        style={{ maxWidth: "100%", height: "auto", objectFit: "contain" }}
                      />
                    </div>
                  ))
                ) : (
                  <div>No images available</div>
                )}
              </Carousel>
            </div>
            <div style={{ flex: 1, padding: '0 20px' }}> {/* Phần dành cho thông tin */}
              <p><strong>Ponds Name:</strong> {selectedShelter?.shelterCategoryName}</p>
              <p><strong>Description:</strong> {selectedShelter?.description}</p>
              <p><strong>Shape:</strong> {selectedShelter?.shape?.shape}</p>
              <p><strong>Width:</strong> {selectedShelter?.width}</p>
              <p><strong>Height:</strong> {selectedShelter?.height}</p>
              <p><strong>Length:</strong> {selectedShelter?.length}</p>
              <p><strong>Diameter:</strong> {selectedShelter?.diameter}</p>
              <p><strong>Water Volume:</strong> {selectedShelter?.waterVolume}</p>
              <p><strong>Water Filtration System:</strong> {selectedShelter?.waterFiltrationSystem}</p>
              <p><strong>Created Date:</strong> {selectedShelter?.createdDate?.toString()}</p>
              <p><strong>Status:</strong> {selectedShelter?.status}</p>

              {/* Destiny Section */}
              <p><strong>Destiny:</strong> {selectedShelter?.shape?.destiny?.destiny || 'No destiny available'}</p>

              {/* Direction of Destiny */}
              <p><strong>Directions:</strong>
                {selectedShelter?.shape?.destiny?.directions && selectedShelter.shape.destiny.directions.length > 0
                  ? selectedShelter.shape.destiny.directions.map((direct: any) => direct.direction).join(', ')
                  : 'No directions available'}
              </p>
            </div>

          </div>
        )}
      </Modal>
    </>
  );
};

export default ShelterCollection;
