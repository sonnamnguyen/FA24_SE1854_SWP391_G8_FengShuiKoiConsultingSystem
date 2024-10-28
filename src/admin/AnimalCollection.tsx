import React, { ChangeEvent, useEffect, useState } from "react";
import AnimalCategory from "../models/AnimalCategory";
import Pagination from "../utils/Pagination";
import { findByAnimalCategory, getAllAnimals } from "./api/AnimalCategoryAPI";
import { Button, Form, Input, Popconfirm, Modal, Upload, Checkbox, Divider, Carousel, notification, UploadFile } from 'antd';
import { Table, Tag, Space } from 'antd';

import { SearchOutlined, PlusOutlined } from '@ant-design/icons';
import api from "../axious/axious";
import { CheckboxProps } from 'antd';
import { ref, uploadString, getDownloadURL, deleteObject } from "firebase/storage";
import { storage } from "../firebase/firebase";
import Color from "../models/Color";

interface Colors {
  id: number;
  color: string;
}
interface AnimalImage {
  id: string; // Sử dụng uid làm id
  imageUrl?: string;
}
const AnimalCollection: React.FC = () => {
  const [listAnimalCategory, setListAnimalCategory] = useState<AnimalCategory[]>([]);
  const [reloadData, setReloadData] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [pageNow, setPageNow] = useState(1);
  const [totalPage, setTotalPage] = useState(0);
  const [name, setName] = useState("");
  const [searchReload, setSearchReload] = useState("");
  const [plainOptions, setPlainOptions] = useState<Colors[]>([]);
  const [checkedList, setCheckedList] = useState<number[]>([]);
  const [animalImages, setAnimalImages] = useState<File[]>([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isUpdateMode, setIsUpdateMode] = useState(false);
  const [selectedAnimal, setSelectedAnimal] = useState<AnimalCategory | null>(null);
  const pageSize = 10;
  const [animalImageMetadata, setAnimalImageMetadata] = useState<{ uid: string; name: string; url: string; }[]>([]);

  const [apii, contextHolder] = notification.useNotification();

  const reloadAnimalList = () => {
    setReloadData(true);

    if (name === "") {
      getAllAnimals()
        .then((animalData) => {
          if (animalData) {
            setListAnimalCategory(animalData.result);
            setTotalPage(animalData.pageTotal);
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
      findByAnimalCategory(name)
        .then((animalData) => {
          if (animalData) {
            setListAnimalCategory(animalData.result);
            setTotalPage(animalData.pageTotal);
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
    const fetchColors = async () => {
      const colors = await getAllColors();
      setPlainOptions(colors || []);
    };
    fetchColors();
    reloadAnimalList();
  }, [pageNow, name]);

  const checkAll = plainOptions.length === checkedList.length;
  const indeterminate = checkedList.length > 0 && checkedList.length < plainOptions.length;

  const onChange = (list: number[]) => {
    setCheckedList(list);
  };

  const onCheckAllChange: CheckboxProps['onChange'] = (e) => {
    setCheckedList(e.target.checked ? plainOptions.map(option => option.id) : []);
  };

  const getAllColors = async (): Promise<Colors[] | null> => {
    try {
      const response = await api.get('/colors/getAll-Colors');
      return response.data.code === 1000 ? response.data.result.map((color: any) => ({ id: color.id, color: color.color })) : null;
    } catch (error) {
      console.error("Error fetching colors: ", error);
      return null;
    }
  };

  const handleView = (id: number) => {
    if (id === undefined) {
      console.error("ID is undefined, cannot delete the animal.");
      return;
    }
    const animal = listAnimalCategory.find(animal => animal.id === id);
    if (animal) {
      setSelectedAnimal(animal);
      setIsModalVisible(true);
      setIsUpdateMode(false);
    }
  };

  const handleUpdate = (id: number) => {
    const animal = listAnimalCategory.find(animal => animal.id === id);
    if (animal) {
      setSelectedAnimal(animal);
      setIsModalVisible(true);
      setIsUpdateMode(true);
      // Set existing color IDs for checkedList
      setCheckedList(animal.colors.map(color => color.id).filter((id): id is number => id !== undefined));

      // Set existing animal images, filtering out undefined image URLs
      setAnimalImages(
        animal.animalImages
          .map((image) => (image.imageUrl ? new File([image.imageUrl], image.imageUrl) : null))
          .filter((file): file is File => file !== null)
      );
    }
  };

  const handleDelete = async (id: number) => {
    try {
      const response = await api.delete(`/animals/${id}`);
      if (response.data.code === 1000) {
        apii.success({ message: 'Success', description: 'Animal has been successfully deleted.' });
        reloadAnimalList();
      } else {
        apii.error({ message: 'Error', description: 'Failed to delete animal.' });
      }
    } catch (error) {
      apii.error({ message: 'Error', description: 'Error deleting animal.' });
    }
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
  };

  const uploadImagesToFirebase = async (files: File[]): Promise<string[]> => {
    const uploadPromises = files
      .filter((file) => file && file.name) // Ensure only valid files with names are processed
      .map(async (file) => {
        const storageRef = ref(storage, `koi_images/${file.name}`);
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

  const handleUploadChange = async (info: { fileList: UploadFile[] }) => {
    const newFiles = info.fileList
      .map((file: UploadFile) => file.originFileObj as File) // Cast originFileObj to File
      .filter((file: File) => !!file); // Filter out invalid files

    // Upload new files to Firebase and get the download URLs
    const uploadedUrls = await uploadImagesToFirebase(newFiles);

    // Update the File objects state
    setAnimalImages((prevImages) => {
      const updatedImages = prevImages.filter((prevFile) =>
        info.fileList.some(
          (file: UploadFile) =>
            file.originFileObj &&
            file.originFileObj.name === prevFile.name &&
            file.originFileObj.size === prevFile.size
        )
      );
      return [...updatedImages, ...newFiles]; // Keep old files and add new ones
    });

    // Update the image metadata state (for preview purposes)
    setAnimalImageMetadata((prevMetadata) => {
      const newMetadata = newFiles.map((file, index) => ({
        uid: `${file.name}-${index}`, // Unique ID for the image
        name: file.name,
        url: uploadedUrls[index], // Firebase download URL
      }));
      return [...prevMetadata, ...newMetadata]; // Always return the array of metadata
    });
  };

  const handleSubmit = async () => {
    try {
      // Upload new images to Firebase
      const base64Avatars = await uploadImagesToFirebase(animalImages);

      // Get the existing animal images and check which ones need deletion
      const existingAnimalImages = selectedAnimal?.animalImages || [];
      const imagesToDelete = existingAnimalImages
        .filter(image => image.imageUrl !== undefined) // Process only images with URLs
        .filter(image => {
          const existingUrl = image.imageUrl;
          if (!existingUrl) return false; // Skip if there's no URL
          const existingImageName = existingUrl.split('/').pop(); // Get the filename from URL
          // Mark images for deletion if not in the new images
          return !base64Avatars
            .map(url => url.split('/').pop())
            .includes(existingImageName || '');
        })
        .map(image => image.imageUrl ?? '')
        .filter(imageUrl => imageUrl !== ''); // Ensure no empty strings



      // Delete old images from the database
      if (imagesToDelete.length > 0) {
        const deleteResponse = await api.post(`/animal-images/${selectedAnimal?.id}`, imagesToDelete);
        if (deleteResponse.data.code !== 1000) {
          apii.error({ message: 'Error', description: 'Failed to delete old images.' });
          return;
        }
      }

      // Build the updated image list
      const updatedAnimalImages = [
        ...existingAnimalImages.filter(image => !imagesToDelete.includes(image.imageUrl ?? '')),
        ...base64Avatars.map(url => ({ imageUrl: url }))
      ];

      // Send update request
      const response = await api.put(`/animals/${selectedAnimal?.id}`, {
        animalCategoryName: selectedAnimal?.animalCategoryName,
        description: selectedAnimal?.description,
        origin: selectedAnimal?.origin,
        animalImages: updatedAnimalImages,
        colors: checkedList.map(id => ({ id }))
      });

      // Handle response
      if (response.data.code === 1000) {
        apii.success({ message: 'Success', description: 'Animal has been successfully updated.' });
        setIsModalVisible(false);
        reloadAnimalList();
      } else {
        apii.error({ message: 'Error', description: 'Failed to update animal.' });
      }
    } catch (error) {
      console.error('Error during update:', error);
      apii.error({ message: 'Error', description: 'Error updating animal.' });
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
      render: (text: string, record: AnimalCategory, index: number) => index + 1 + (pageNow - 1) * pageSize,
    },
    {
      title: 'Image',
      dataIndex: 'animalImages',
      key: 'animalImages',
      render: (images: any) => (
        <img
          src={images && images.length > 0 ? images[0].imageUrl : "path_to_placeholder.jpg"}
          alt="Koi"
          style={{ width: "100px", height: "100px", objectFit: "cover" }}
        />
      ),
    },
    {
      title: 'Koi Name',
      dataIndex: 'animalCategoryName',
      key: 'animalCategoryName',
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
      render: (text: string, record: AnimalCategory) => (
        <Space size="middle">
          <Button
            type="primary"
            onClick={() => {
              if (record.id !== undefined) {
                handleView(record.id);
              } else {
                console.error("Animal ID is undefined, cannot view.");
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
                console.error("Animal ID is undefined, cannot update.");
              }
            }}
          >
            Update
          </Button>
          <Popconfirm
            title="Delete the animal"
            okText="Yes"
            cancelText="No"
            onConfirm={() => {
              if (record.id !== undefined) {
                handleDelete(record.id);
              } else {
                console.error("Animal ID is undefined, cannot delete.");
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
          Add Animal
        </Button>
      </div>
      <Table
        columns={columns}
        dataSource={listAnimalCategory}
        pagination={false}
        rowKey="id"
      />
      <Pagination
        currentPage={pageNow}
        totalPages={totalPage}
        pagination={pagination}
      />
      <Modal
        title={isUpdateMode ? "Update Animal Details" : "Animal Details"}
        open={isModalVisible}
        onOk={isUpdateMode ? handleSubmit : handleModalCancel}
        onCancel={handleModalCancel}
        width={1000}
        style={{ top: '15%' }}
      >
        {isUpdateMode ? (
          <Form onFinish={handleSubmit}>
            <Form.Item
              label="Name"
              required
              rules={[{ required: true, message: 'Please input the animal name!' }]}
            >
              <Input
                value={selectedAnimal?.animalCategoryName}
                onChange={(e) => setSelectedAnimal((prev) => prev && { ...prev, animalCategoryName: e.target.value })}
              />
            </Form.Item>

            <Form.Item
              label="Description"
              required
              rules={[{ required: true, message: 'Please input the description!' }]}
            >
              <Input
                value={selectedAnimal?.description}
                onChange={(e) => setSelectedAnimal((prev) => prev && { ...prev, description: e.target.value })}
              />
            </Form.Item>

            <Form.Item
              label="Origin"
              required
              rules={[{ required: true, message: 'Please input the origin!' }]}
            >
              <Input
                value={selectedAnimal?.origin}
                onChange={(e) => setSelectedAnimal((prev) => prev && { ...prev, origin: e.target.value })}
              />
            </Form.Item>

            <Form.Item label="Avatars">
              <Upload
                multiple
                accept="image/*"
                showUploadList={true}
                beforeUpload={() => false} // Prevent automatic upload
                listType="picture-card"
                fileList={animalImageMetadata.map((file) => ({
                  uid: file.uid, // Use UID for identification
                  name: file.name, // File name
                  status: 'done', // Mark as done after upload
                  url: file.url, // Firebase URL for preview
                }))}
                onChange={handleUploadChange}
                onRemove={async (file: UploadFile) => {
                  // Remove image from Firebase Storage
                  const imageRef = ref(storage, `koi_images/${file.name}`);
                  await deleteObject(imageRef);

                  // Update the state with the filtered array
                  setAnimalImageMetadata((prevMetadata) =>
                    prevMetadata.filter((img) => img.name !== file.name)
                  );

                  // Also remove the file from animalImages state
                  setAnimalImages((prevFiles) =>
                    prevFiles.filter((img) => img.name !== file.name)
                  );
                }}
              >
                <div>
                  <PlusOutlined />
                  <div style={{ marginTop: 8 }}>Upload</div>
                </div>
              </Upload>
            </Form.Item>

            <Form.Item>
              <Checkbox indeterminate={indeterminate} onChange={onCheckAllChange} checked={checkAll}>
                Check all
              </Checkbox>
              <Divider />
              <Checkbox.Group
                options={plainOptions.map(option => ({ label: option.color, value: option.id }))}
                value={checkedList} // Value from checkedList
                onChange={onChange}
              />
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
                {selectedAnimal?.animalImages?.length ? (
                  selectedAnimal.animalImages.map((image, index) => (
                    <div key={index}>
                      <img
                        src={image.imageUrl}
                        alt={`Image of ${selectedAnimal?.animalCategoryName}`}
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
              <p><strong>Koi Name:</strong> {selectedAnimal?.animalCategoryName}</p>
              <p><strong>Description:</strong> {selectedAnimal?.description}</p>
              <p><strong>Origin:</strong> {selectedAnimal?.origin}</p>
              <p><strong>Created Date:</strong> {selectedAnimal?.createdDate?.toString()}</p>
              <p><strong>Status:</strong> {selectedAnimal?.status}</p>
              <p><strong>Colors:</strong> {selectedAnimal?.colors?.map((color: any) => color.color).join(', ')}</p>
              <p>
                <strong>Destiny:</strong> {
                  selectedAnimal?.colors
                    ?.flatMap((color: Color) => color.destiny ? color.destiny.destiny : 'No destiny available')
                    .filter((value, index, self) => self.indexOf(value) === index)
                    .join(', ')
                }
              </p>

              <p>
                <strong>Numbers:</strong> {
                  selectedAnimal?.colors
                    ?.flatMap((color: Color) => color.destiny?.numbers ? color.destiny.numbers : [])
                    .filter((value, index, self) => self.findIndex(num => num.id === value.id) === index)
                    .map((number: any) => number.number) // Lấy giá trị number từ object
                    .join(', ')
                }
              </p>


            </div>
          </div>
        )}
      </Modal>
    </>
  );
};

export default AnimalCollection;
