import React, { ChangeEvent, useEffect, useState } from "react";
import { Button, Form, Input, Popconfirm, Modal, Space, Table, Radio, notification } from 'antd';
import { SearchOutlined, PlusOutlined } from '@ant-design/icons';
import api from "../axious/axious";
import Pagination from "../utils/Pagination";
import Color from "../models/Color";
import { findByColor, getAllColors } from "./api/ColorAPI";

interface Destinys {
  id: number;
  destiny: string;
}

const ColorCollection: React.FC = () => {
  const [listColor, setListColor] = useState<Color[]>([]);
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
  const [selectedColor, setSelectedColor] = useState<Color | null>(null);
  const pageSize = 10;

  const [apii, contextHolder] = notification.useNotification();

  const reloadColorList = () => {
    setReloadData(true);

    if (name === "") {
      getAllColors()
        .then((colorData) => {
          if (colorData) {
            setListColor(colorData.result);
            setTotalPage(colorData.pageTotal);
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
      findByColor(name)
        .then((colorData) => {
          if (colorData) {
            setListColor(colorData.result);
            setTotalPage(colorData.pageTotal);
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
    reloadColorList();
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
    const color = listColor.find(color => color.id === id);
    if (color) {
      setSelectedColor(color);
      setSelectedDestiny(color.destiny?.id || null);
      setIsModalVisible(true);
      setIsUpdateMode(true);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      const response = await api.delete(`/colors/${id}`);
      if (response.data.code === 1000) {
        apii.success({ message: 'Success', description: 'Color has been successfully deleted.' });
        reloadColorList();
      } else {
        apii.error({ message: 'Error', description: 'Failed to delete color.' });
      }
    } catch (error) {
      apii.error({ message: 'Error', description: 'Error deleting color.' });
    }
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
    setSelectedColor(null);
    setSelectedDestiny(null);
  };

  const handleSubmit = async () => {
    try {
      const response = await api.put(`/colors/${selectedColor?.id}`, {
        color: selectedColor?.color,
        destiny: { id: selectedDestiny },
      });

      if (response.data.code === 1000) {
        apii.success({ message: 'Success', description: 'Color has been successfully added.' });
        setIsModalVisible(false);
        reloadColorList();
      } else {
        apii.error({ message: 'Error', description: 'Failed to add color.' });
      }
    } catch (error) {
      apii.error({ message: 'Error', description: 'Error adding color.' });
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
      render: (text: string, record: Color, index: number) => index + 1 + (pageNow - 1) * pageSize,
    },
    {
      title: 'Color',
      dataIndex: 'color',
      key: 'color',
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
      render: (text: string, record: Color) => (
        <Space size="middle">
          <Button
            onClick={() => {
              if (record.id !== undefined) {
                handleUpdate(record.id);
              } else {
                console.error("Color ID is undefined, cannot update.");
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
                console.error("Color ID is undefined, cannot delete.");
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
          Add Color
        </Button>
      </div>
      <Table
        columns={columns}
        dataSource={listColor}
        pagination={false}
        rowKey="id"
      />
      <Pagination
        currentPage={pageNow}
        totalPages={totalPage}
        pagination={pagination}
      />
      <Modal
        title={isUpdateMode ? "Update Color" : "Add Color"}
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
            rules={[{ required: true, message: 'Please input the color name!' }]}
          >
            <Input
              value={selectedColor?.color}
              onChange={(e) => setSelectedColor((prev) => prev && { ...prev, color: e.target.value })}
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

export default ColorCollection;
