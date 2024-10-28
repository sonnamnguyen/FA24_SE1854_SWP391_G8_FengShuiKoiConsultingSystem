import React, { ChangeEvent, useEffect, useState } from "react";
import { Button, Form, Input, Popconfirm, Modal, Space, Table, Radio, notification } from 'antd';
import { SearchOutlined, PlusOutlined } from '@ant-design/icons';
import api from "../axious/axious";
import Pagination from "../utils/Pagination";
import Color from "../models/Color";
import { findByColor, getAllColors } from "./api/ColorAPI";
import Shape from "../models/Shape";
import { findByShape, getAllShapes } from "./api/ShapeAPI";

interface Destinys {
  id: number;
  destiny: string;
}

const ShapeCollection: React.FC = () => {
  const [listShape, setListShape] = useState<Shape[]>([]);
  const [reloadData, setReloadData] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [pageNow, setPageNow] = useState(1);
  const [totalPage, setTotalPage] = useState(0);
  const [name, setColor] = useState("");
  const [searchReload, setSearchReload] = useState("");
  const [destinyOptions, setDestinyOptions] = useState<Destinys[]>([]);
  const [selectedDestiny, setSelectedDestiny] = useState<number | null>(null);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isUpdateMode, setIsUpdateMode] = useState(false);
  const [selectedShape, setSelectedShape] = useState<Shape | null>(null);
  const pageSize = 10;

  const [apii, contextHolder] = notification.useNotification();

  const reloadShapeList = () => {
    setReloadData(true);

    if (name === "") {
      getAllShapes()
        .then((shapeData) => {
          if (shapeData) {
            setListShape(shapeData.result);
            setTotalPage(shapeData.pageTotal);
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
      findByShape(name)
        .then((shapeData) => {
          if (shapeData) {
            setListShape(shapeData.result);
            setTotalPage(shapeData.pageTotal);
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
    const fetchDestinies = async () => {
      const destinies = await getAllDestinies();
      if (destinies) {
        setDestinyOptions(destinies);
      }
    };
    fetchDestinies();
    reloadShapeList();
  }, [pageNow, name]);

  const getAllDestinies = async (): Promise<Destinys[] | null> => {
    try {
      const response = await api.get(`/destinys`);
      if (response.data.code === 1000) {
        return response.data.result.map((destiny: any) => ({
          id: destiny.id,
          destiny: destiny.destiny,
        })) as Destinys[];
      }
      console.error("Failed to fetch destinies: ", response.status);
      return null;
    } catch (error) {
      console.error("Error fetching destinies: ", error);
      return null;
    }
  };

  const handleUpdate = (id: number) => {
    const color = listShape.find(shape => shape.id === id);
    if (color) {
      setSelectedShape(color);
      setSelectedDestiny(color.destiny?.id || null);
      setIsModalVisible(true);
      setIsUpdateMode(true);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      const response = await api.delete(`/shapes/${id}`);
      if (response.data.code === 1000) {
        apii.success({ message: 'Success', description: 'Shape has been successfully deleted.' });
        reloadShapeList();
      } else {
        apii.error({ message: 'Error', description: 'Failed to delete shape.' });
      }
    } catch (error) {
      apii.error({ message: 'Error', description: 'Error deleting shape.' });
    }
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
    setSelectedShape(null);
    setSelectedDestiny(null);
  };

  const handleSubmit = async () => {
    try {
      const response = await api.put(`/shapes/${selectedShape?.id}`, {
        shape: selectedShape?.shape,
        destiny: { id: selectedDestiny },
      });

      if (response.data.code === 1000) {
        apii.success({ message: 'Success', description: 'Shape has been successfully added.' });
        setIsModalVisible(false);
        reloadShapeList();
      } else {
        apii.error({ message: 'Error', description: 'Failed to add shape.' });
      }
    } catch (error) {
      apii.error({ message: 'Error', description: 'Error adding shape.' });
    }
  };

  const pagination = (page: number) => {
    setPageNow(page);
  };

  const handleSearch = () => {
    setColor(searchReload);
    setReloadData(true);
    setPageNow(1);
  };

  const columns = [
    {
      title: 'STT',
      dataIndex: 'index',
      key: 'index',
      render: (text: string, record: Shape, index: number) => index + 1 + (pageNow - 1) * pageSize,
    },
    {
      title: 'Shape',
      dataIndex: 'shape',
      key: 'shape',
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
      title: 'Destiny',
      dataIndex: 'destiny',
      key: 'destiny',
      render: (destiny: { id: number; destiny: string }) => (
        <span
          title={destiny?.destiny} // Accessing the destiny property
          style={{ display: 'block', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', maxWidth: '150px' }}
        >
          {destiny?.destiny} {/* Display the specific property */}
        </span>
      ),
    },
    {
      title: 'Action',
      key: 'action',
      render: (text: string, record: Shape) => (
        <Space size="middle">
          <Button
            onClick={() => {
              if (record.id !== undefined) {
                handleUpdate(record.id);
              } else {
                console.error("Shape ID is undefined, cannot update.");
              }
            }}
          >
            Update
          </Button>
          <Popconfirm
            title="Delete the color"
            okText="Yes"
            cancelText="No"
            onConfirm={() => {
              if (record.id !== undefined) {
                handleDelete(record.id);  // Change this to handleDelete
              } else {
                console.error("Shape ID is undefined, cannot delete.");
              }
            }}
          >
            <Button type="primary" danger>
              Delete
            </Button>
          </Popconfirm>
        </Space>
      ),
    }
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
          Add Shape
        </Button>
      </div>
      <Table
        columns={columns}
        dataSource={listShape}
        pagination={false}
        rowKey="id"
      />
      <Pagination
        currentPage={pageNow}
        totalPages={totalPage}
        pagination={pagination}
      />
      <Modal
        title={isUpdateMode ? "Update Shape" : "Add Shape"}
        open={isModalVisible}
        onOk={handleSubmit}
        onCancel={handleModalCancel}
        width={1000}
        style={{ top: '15%' }}
      >
        <Form onFinish={handleSubmit}>
          <Form.Item
            label="Name"
            required
            rules={[{ required: true, message: 'Please input the shape name!' }]}
          >
            <Input
              value={selectedShape?.shape}
              onChange={(e) => setSelectedShape((prev) => prev && { ...prev, shape: e.target.value })}
            />
          </Form.Item>
          <Form.Item label="Destiny">
            <Radio.Group
              onChange={e => setSelectedDestiny(e.target.value)}
              value={selectedDestiny}
            >
              {destinyOptions.map(destiny => (
                <Radio key={destiny.id} value={destiny.id}>
                  {destiny.destiny}
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
      </Modal>
    </>
  );
};

export default ShapeCollection;
