import React, { ChangeEvent, useEffect, useState } from "react";
import { Button, Form, Input, Popconfirm, Modal, Upload, Checkbox, Divider, Carousel, notification, UploadFile, Pagination } from 'antd';
import { Table, Tag, Space } from 'antd';
import { Radio } from 'antd';
import { SearchOutlined, PlusOutlined } from '@ant-design/icons';
import api from "../axious/axious";
import { CheckboxProps } from 'antd';
import { ref, uploadString, getDownloadURL, deleteObject } from "firebase/storage";
import { storage } from "../firebase/firebase";
import Color from "../models/Color";
import ShelterCategory from "../models/ShelterCategory";
import { findByShelterCategory, getAllShelters } from "./api/ShelterCategoryAPI";
import Shape from "../models/Shape";
import DestinyTuongSinh from "../models/DestinyTuongSinh";
import DestinyTuongKhac from "../models/DestinyTuongKhac";

interface ShelterCollectionProps {
  setIsNavbarVisible: (visible: boolean) => void;
}

interface Shapes {
  id: number;
  shape: string;
}

const ShelterCollection: React.FC<ShelterCollectionProps> = ({ setIsNavbarVisible }) => {
  const [listShelterCategory, setListShelterCategory] = useState<ShelterCategory[]>([]);
  const [reloadData, setReloadData] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [pageNow, setPageNow] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [name, setName] = useState("");
  const [searchReload, setSearchReload] = useState("");
  const [plainOptions, setPlainOptions] = useState<Shapes[]>([]);
  const [checkedList, setCheckedList] = useState<number[]>([]);
  const [shelterImages, setShelterImages] = useState<File[]>([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isUpdateMode, setIsUpdateMode] = useState(false);
  const [selectedShelter, setSelectedShelter] = useState<ShelterCategory | null>(null);
  const [selectedShape, setSelectedShape] = useState<number | null>(null);
  const [animalImageMetadata, setAnimalImageMetadata] = useState<{ uid: string; name: string; url: string; }[]>([]);
  const [destinyToTuongSinhMap, setDestinyToTuongSinhMap] = useState<{ [key: string]: DestinyTuongSinh[] }>({});
  const [destinyToTuongKhacMap, setDestinyToTuongKhacMap] = useState<{ [key: string]: DestinyTuongKhac[] }>({});
  const pageSize = 10;

  const [apii, contextHolder] = notification.useNotification();

  const reloadShelterList = (page: number = 1) => {
    setReloadData(true);

    if (name === "") {
      getAllShelters(page, pageSize)
        .then((shelterData) => {
          if (shelterData) {
            setListShelterCategory(shelterData.result);
            setTotalElements(shelterData.totalElements);
            setPageNow(page);
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
      findByShelterCategory(name, page, pageSize)
        .then((shelterData) => {
          if (shelterData) {
            setListShelterCategory(shelterData.result);
            setTotalElements(shelterData.totalElements);
            setPageNow(page);
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
    reloadShelterList(pageNow);
  }, [pageNow, name]);


  const onPaginationChange = (page: number) => {
    setPageNow(page); // Cập nhật trạng thái để hiển thị trang mới
    reloadShelterList(page); // Gọi lại API với trang mới
  };

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
      setIsNavbarVisible(false);
    }
  };

  const handleUpdate = async (id: number) => {
    const shelter = listShelterCategory.find(shelter => shelter.id === id);
    if (shelter) {
      setSelectedShelter(shelter);
      setIsModalVisible(true);
      setIsUpdateMode(true);
      setIsNavbarVisible(false);
      setSelectedShape(shelter.shape?.id || null)
      // Check if shelterImages exists and is an array
      if (shelter.shelterImages && Array.isArray(shelter.shelterImages)) {
        const validImageMetadata = await Promise.all(
          shelter.shelterImages.map(async (image, index) => {
            if (image.imageUrl) {
              try {
                // Parse and extract the actual storage path from full Firebase URLs
                const imagePath = image.imageUrl.startsWith("https://")
                  ? decodeURIComponent(image.imageUrl.split("/o/")[1].split("?")[0])
                  : image.imageUrl; // If already a storage path, use directly

                // Attempt to retrieve download URL for the parsed path
                await getDownloadURL(ref(storage, imagePath));

                return {
                  uid: `${image.imageUrl}-${index}`,
                  name: `Image-${index + 1}`,
                  url: image.imageUrl,
                };
              } catch (error) {
                console.warn(`Image not found: ${image.imageUrl}`, error);
                return null; // Skip if image doesn't exist
              }
            }
            return null;
          })
        );

        setAnimalImageMetadata(
          validImageMetadata.filter((img): img is { uid: string; name: string; url: string } => img !== null)
        );
      } else {
        console.warn('No images found for this shelter.');
        // Optionally, handle the case where shelterImages is undefined or not an array
        setAnimalImageMetadata([]); // or any other fallback
      }
    }
  };

  const handleDelete = async (id: number) => {
    try {
      const response = await api.delete(`/shelters/${id}`);
      if (response.data.code === 1000) {
        apii.success({ message: 'Success', description: 'Shelter has been successfully deleted.' });
        reloadShelterList();
      } else {
        apii.error({ message: 'Error', description: response.data.message });
      }
    } catch (error: any) {
      apii.error({ message: 'Error', description: error.response.data.message });
    }
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
    setIsNavbarVisible(true);
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


  const handleUploadChange = async (info: { fileList: UploadFile[] }) => {
    const newFiles = info.fileList
      .map((file: UploadFile) => file.originFileObj as File) // Cast originFileObj to File
      .filter((file: File) => !!file); // Filter out invalid files

    // Upload new files to Firebase and get the download URLs
    const uploadedUrls = await uploadImagesToFirebase(newFiles);

    // Update the File objects state
    setShelterImages((prevImages) => {
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
    const base64Avatars = await uploadImagesToFirebase(shelterImages);
    const existingAnimalImages = selectedShelter?.shelterImages || [];
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
      const deleteResponse = await api.post(`/shelter-images/${selectedShelter?.id}`, imagesToDelete);
      if (deleteResponse.data.code !== 1000) {
        apii.error({ message: 'Error', description: 'Failed to delete old images.' });
        return;
      }
    }

    // Build the updated image list
    const updatedShelterImages = [
      ...existingAnimalImages.filter(image => !imagesToDelete.includes(image.imageUrl ?? '')),
      ...base64Avatars.map(url => ({ imageUrl: url }))
    ];

    try {
      const response = await api.put(`/shelters/${selectedShelter?.id}`, {
        shelterCategoryName: selectedShelter?.shelterCategoryName,
        description: selectedShelter?.description,
        shape: { id: selectedShape },
        width: selectedShelter?.width,
        height: selectedShelter?.height,
        length: selectedShelter?.length,
        diameter: selectedShelter?.diameter,
        waterVolume: selectedShelter?.waterVolume,
        waterFiltrationSystem: selectedShelter?.waterFiltrationSystem,
        shelterImages: updatedShelterImages,
      });

      if (response.data.code === 1000) {
        apii.success({ message: 'Success', description: 'Shelter has been successfully updated.' });
        setIsModalVisible(false);
        reloadShelterList();
      } else {
        apii.error({ message: 'Error', description: response.data.message });
      }
    } catch (error: any) {
      apii.error({ message: 'Error', description: error.response.data.message });
    }
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
  const fetchDestinyTuongSinh = async (destinyName: string) => {
    try {
      const response = await api.get(`/destinys/${destinyName}`);
      if (response.data.code === 1000) {
        const tuongSinhData = response.data.result.destinyTuongSinhs;
        const tuongKhacData = response.data.result.destinyTuongKhacs;

        const newDestinyTuongSinhs = tuongSinhData.map((item: any) => new DestinyTuongSinh(item.name));
        const newDestinyTuongKhac = tuongKhacData.map((item: any) => new DestinyTuongKhac(item.name));

        // Update the map with the new data for the specific destiny
        setDestinyToTuongSinhMap((prev) => ({
          ...prev,
          [destinyName]: newDestinyTuongSinhs,
        }));
        setDestinyToTuongKhacMap((prev) => ({
          ...prev,
          [destinyName]: newDestinyTuongKhac,
        }));
      }
    } catch (error) {
      console.error("Error fetching DestinyTuongSinh:", error);
    }
  };



  useEffect(() => {
    const destinyName = selectedShelter?.shape?.destiny?.destiny;
    if (destinyName) {
      fetchDestinyTuongSinh(destinyName);
    }
  }, [selectedShelter?.shape?.destiny?.destiny]);
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
        <Button type="primary" icon={<PlusOutlined />} onClick={() => {
          setIsModalVisible(true);
          setIsNavbarVisible(false);
        }}>
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
        current={pageNow}
        total={totalElements}
        pageSize={pageSize}
        onChange={onPaginationChange}
        style={{ textAlign: 'end', marginTop: '20px' }}
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
                  const imageRef = ref(storage, `shelter_images/${file.name}`);
                  await deleteObject(imageRef);

                  // Update the state with the filtered array
                  setAnimalImageMetadata((prevMetadata) =>
                    prevMetadata.filter((img) => img.name !== file.name)
                  );

                  // Also remove the file from animalImages state
                  setShelterImages((prevFiles) =>
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
          <div style={{ display: "flex", gap: "20px" }}>
            <div style={{ flex: 1, maxHeight: '400px', overflow: 'hidden' }}>
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
              <p><strong>Width:</strong> {selectedShelter?.width} m</p>
              <p><strong>Height:</strong> {selectedShelter?.height} m</p>
              <p><strong>Length:</strong> {selectedShelter?.length} m</p>
              <p><strong>Diameter:</strong> {selectedShelter?.diameter} m</p>
              <p><strong>Water Volume:</strong> {selectedShelter?.waterVolume} L</p>
              <p><strong>Water Filtration System:</strong> {selectedShelter?.waterFiltrationSystem}</p>
              <p><strong>Created Date:</strong> {
                selectedShelter?.createdDate
                  ? new Date(selectedShelter.createdDate).toLocaleDateString('en-GB')
                  : 'No date available'
              }</p>
              <p><strong>Status:</strong> {selectedShelter?.status}</p>

              {/* Destiny Section */}
              <p><strong>Result :</strong></p>
              {/* Direction of Destiny */}
              <p><strong>Directions:</strong>
                {selectedShelter?.shape?.destiny?.directions && selectedShelter.shape.destiny.directions.length > 0
                  ? selectedShelter.shape.destiny.directions.map((direct: any) => direct.direction).join(', ')
                  : 'No directions available'}
              </p>
              <p style={{ color: 'blue' }}><strong>Mutual Accord:</strong> {selectedShelter?.shape?.destiny?.destiny || 'No destiny available'}</p>
              <p style={{ color: 'green' }}><strong>Mutual Generation:</strong>
                {(() => {
                  const destinyName = selectedShelter?.shape?.destiny?.destiny;
                  const tuongSinhList = destinyName ? destinyToTuongSinhMap[destinyName] || [] : [];
                  const tuongSinhNames = tuongSinhList.map((tuongSinh) => tuongSinh.name).join(', ');
                  return tuongSinhNames || "No data available";
                })()}
              </p>
              <p style={{ color: 'red' }}><strong>Mutual Overcoming:</strong>
                {(() => {
                  const destinyName = selectedShelter?.shape?.destiny?.destiny;
                  const tuongKhacList = destinyName ? destinyToTuongKhacMap[destinyName] || [] : [];
                  const tuongKhacNames = tuongKhacList.map((tuongKhac) => tuongKhac.name).join(', ');
                  return tuongKhacNames || "No data available";
                })()}
              </p>

            </div>

          </div>
        )}
      </Modal>
    </>
  );
};

export default ShelterCollection;
