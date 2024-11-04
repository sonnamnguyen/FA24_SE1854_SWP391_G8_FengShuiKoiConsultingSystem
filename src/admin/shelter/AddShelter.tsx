import React, { useEffect, useState } from "react";
import { notification, Upload, Form, Input, Radio } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { ref, uploadString, getDownloadURL } from "firebase/storage";
import { storage } from "../../firebase/firebase";
import { getToken } from "../../service/localStorageService";
import '../../css/addKoi.css';
import api from "../../axious/axious";

interface Shapes {
  id: number;
  shape: string;
}

const AddShelter: React.FC = () => {
  const [shelterCategoryName, setShelterCategoryName] = useState("");
  const [selectedShape, setSelectedShape] = useState<number | null>(null);
  const [width, setWidth] = useState(0);
  const [height, setHeight] = useState(0);
  const [length, setLength] = useState(0);
  const [diameter, setDiameter] = useState(0);
  const [waterVolume, setWaterVolume] = useState(0);
  const [waterFiltrationSystem, setWaterFiltrationSystem] = useState("");
  const [description, setDescription] = useState("");
  const [shelterImages, setShelterImages] = useState<File[]>([]);
  const [apii, contextHolder] = notification.useNotification();
  const [plainOptions, setPlainOptions] = useState<Shapes[]>([]);

  useEffect(() => {
    const fetchShapes = async () => {
      const shapes = await getAllShapes();
      setPlainOptions(shapes || []);
    };
    fetchShapes();
  }, []);

  const getAllShapes = async (): Promise<Shapes[] | null> => {
    try {
      const response = await api.get('/shapes/getAll-Shapes');
      return response.data.code === 1000
        ? response.data.result.map((shape: any) => ({ id: shape.id, shape: shape.shape }))
        : null;
    } catch (error) {
      console.error("Error fetching shapes: ", error);
      return null;
    }
  };

  const uploadImagesToFirebase = async (files: File[]): Promise<string[]> => {
    const uploadPromises = files.map(async (file) => {
      const storageRef = ref(storage, `shelter_images/${file.name}`);
      const base64Image = await getBase64(file);
      await uploadString(storageRef, base64Image, 'data_url');
      const downloadURL = await getDownloadURL(storageRef);
      return downloadURL;
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

  const handleSubmit = async () => {
    if (width < 0 || height < 0 || length < 0 || diameter < 0 || waterVolume < 0) {
      apii.error({ message: 'Error', description: 'Dimensions must be non-negative.' });
      return;
    }

    const base64Images = shelterImages.length > 0 ? await uploadImagesToFirebase(shelterImages) : [];
    try {
      const response = await api.post("/shelters", {
          shelterCategoryName,
          description,
          shape: { id: selectedShape },
          width,
          height,
          length,
          diameter,
          waterVolume,
          waterFiltrationSystem,
          shelterImages: base64Images.map(url => ({ imageUrl: url })),
    
      });

      if (response.data.code === 1000) {
        apii.success({ message: 'Success', description: 'Shelter has been successfully added.' });
        // Optionally reset the form here
      } else {
        apii.error({ message: 'Error', description: response.data.message });
      }
    } catch (error) {
      console.error(error); // Log error for debugging
      apii.error({ message: 'Error', description: `Error adding shelter. Please try again later.` });
    }
  };

  const handleUploadChange = (info: any) => {
    const { fileList } = info;
    setShelterImages(fileList.map((file: any) => file.originFileObj));
  };

  return (
    <div className="container">
      <h1 className="mt-5">Add Shelter</h1>
      <Form onFinish={handleSubmit}>
        <Form.Item label="Pond Name" required rules={[{ required: true, message: 'Please input the pond name!' }]}>
          <Input value={shelterCategoryName} onChange={(e) => setShelterCategoryName(e.target.value)} />
        </Form.Item>

        <Form.Item label="Description" required rules={[{ required: true, message: 'Please input the description!' }]}>
          <Input value={description} onChange={(e) => setDescription(e.target.value)} />
        </Form.Item>

        <Form.Item label="Width" required rules={[{ required: true, message: 'Please input width!' }]}>
          <Input type="number" value={width} onChange={(e) => setWidth(parseFloat(e.target.value))} />
        </Form.Item>

        <Form.Item label="Height" required rules={[{ required: true, message: 'Please input height!' }]}>
          <Input type="number" value={height} onChange={(e) => setHeight(parseFloat(e.target.value))} />
        </Form.Item>

        <Form.Item label="Length" required rules={[{ required: true, message: 'Please input length!' }]}>
          <Input type="number" value={length} onChange={(e) => setLength(parseFloat(e.target.value))} />
        </Form.Item>

        <Form.Item label="Diameter" required rules={[{ required: true, message: 'Please input diameter!' }]}>
          <Input type="number" value={diameter} onChange={(e) => setDiameter(parseFloat(e.target.value))} />
        </Form.Item>

        <Form.Item label="Water Volume" required rules={[{ required: true, message: 'Please input the water volume!' }]}>
          <Input type="number" value={waterVolume} onChange={(e) => setWaterVolume(parseFloat(e.target.value))} />
        </Form.Item>

        <Form.Item label="Water Filtration System" required rules={[{ required: true, message: 'Please input Water Filtration System!' }]}>
          <Input value={waterFiltrationSystem} onChange={(e) => setWaterFiltrationSystem(e.target.value)} />
        </Form.Item>

        <Form.Item label="Images">
          <Upload
            multiple
            accept="image/*"
            showUploadList={true}
            beforeUpload={() => false}
            onChange={handleUploadChange}
            listType="picture-card"
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
            value={selectedShape}
          >
            {plainOptions.map(shape => (
              <Radio key={shape.id} value={shape.id}>
                {shape.shape}
              </Radio>
            ))}
          </Radio.Group>
        </Form.Item>

        <Form.Item>
          <button type="submit" className="btn-primary">Submit</button>
        </Form.Item>
      </Form>
      {contextHolder}
    </div>
  );
};

export default AddShelter;
