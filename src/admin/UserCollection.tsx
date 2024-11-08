import React, { useEffect, useState, ChangeEvent } from "react";
import { Table, Button, Popconfirm, Input, Space, Modal, Form, Checkbox, Divider, Pagination, notification } from "antd";
import { SearchOutlined } from "@ant-design/icons";
import { findByUserName, getAllUsers } from "./api/UserAPI";
import api from "../axious/axious";
import User from "../models/User";
import Role from "../models/Role";

interface UserCollectionProps {
  setIsNavbarVisible: (visible: boolean) => void; 
}

const UserCollection: React.FC<UserCollectionProps> = ({ setIsNavbarVisible }) => {
  const [listUser, setListUser] = useState<User[]>([]);
  const [reloadData, setReloadData] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [pageNow, setPageNow] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [name, setName] = useState("");
  const [searchReload, setSearchReload] = useState("");
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [plainOptions, setPlainOptions] = useState<Role[]>([]);
  const [checkedList, setCheckedList] = useState<number[]>([]);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
  const pageSize = 10;

  const [apii, contextHolder] = notification.useNotification();

  const reloadUserList = (page: number = 1) => {
    setReloadData(true);

    if (name === "") {
      getAllUsers(page, pageSize)
        .then((userData) => {
          if (userData) {
            setListUser(userData.result);
            setTotalElements(userData.totalElements);
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
      findByUserName(name, page, pageSize)
        .then((userData) => {
          if (userData) {
            setListUser(userData.result);
            setTotalElements(userData.totalElements);
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

  const checkAll = plainOptions.length === checkedList.length;
  const indeterminate = checkedList.length > 0 && checkedList.length < plainOptions.length;

  const onChange = (list: number[]) => {
    setCheckedList(list);
  };

  const onCheckAllChange = (e: any) => {
    setCheckedList(e.target.checked ? plainOptions.map(option => option.id).filter(id => id !== undefined) as number[] : []);
  };

  useEffect(() => {
    const fetchRoles = async () => {
      const roles = await getAllRoles();
      setPlainOptions(roles || []);
    };
    fetchRoles();
    reloadUserList(pageNow);
  }, [pageNow, name]);
  const onPaginationChange = (page: number) => {
    setPageNow(page); // Cập nhật trạng thái để hiển thị trang mới
    reloadUserList(page); // Gọi lại API với trang mới
};
  const handleDelete = async (id: number) => {
    try {
      const response = await api.delete(`/users/${id}`);
      if (response.data.code === 1000) {
        apii.success({ message: "Success", description: "User has been successfully deleted." });
        reloadUserList();
      } else {
        apii.error({ message: "Error", description: "Failed to delete user." });
      }
    } catch (error) {
      apii.error({ message: "Error", description: "Error deleting user." });
    }
  };

  const handleSubmit = async () => {
    
    if (selectedUserId === null) return;
    try {
      const response = await api.post(`/users/${selectedUserId}/set-roles`, { roles: checkedList.map(id => ({id})) 
    });
      if (response.data.code === 1000) {
        apii.success({ message: "Success", description: "Roles updated successfully." });
        setIsModalVisible(false);
        reloadUserList();
      } else {
        apii.error({ message: "Error", description: "Failed to update roles." });
      }
    } catch (error) {
      apii.error({ message: "Error", description: "Error updating roles." });
    }
  };

  const getAllRoles = async (): Promise<Role[] | null> => {
    try {
      const response = await api.get("/roles");
      return response.data.code === 1000
        ? response.data.result.map((role: any) => new Role(role.id, role.name))  // Construct Role with both id and name
        : null;
    } catch (error) {
      console.error("Error fetching roles: ", error);
      return null;
    }
  };
  const handleModalCancel = () => {
    setIsModalVisible(false);
    setIsNavbarVisible(true);
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
      title: "STT",
      dataIndex: "index",
      key: "index",
      render: (text: string, record: User, index: number) => index + 1 + (pageNow - 1) * pageSize,
    },
    {
      title: "UserName",
      dataIndex: "userName",
      key: "userName",
      render: (text: string) => (
        <span
          title={text}
          style={{ display: "block", whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis", maxWidth: "150px" }}
        >
          {text}
        </span>
      ),
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      render: (text: string) => (
        <span
          title={text}
          style={{ display: "block", whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis", maxWidth: "150px" }}
        >
          {text}
        </span>
      ),
    },
    {
      title: "PhoneNumber",
      dataIndex: "phoneNumber",
      key: "phoneNumber",
      render: (text: string) => (
        <span
          title={text}
          style={{ display: "block", whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis", maxWidth: "150px" }}
        >
          {text}
        </span>
      ),
    },
    {
      title: "Roles",
      dataIndex: "roles", // Ensure the dataIndex is correctly mapped to the 'roles' field in your data
      key: "roles",
      render: (roles: Role[] | undefined) => {
        if (Array.isArray(roles)) {
          return roles.map((role: Role) => role.name).join(", ");
        } else {
          return roles ? (roles as any).name || roles : "No roles"; // Handle cases where roles might not be an array
        }
      },
    },
    {
      title: "Action",
      key: "action",
      render: (text: string, record: User) => (
        <Space size="middle">
          <Button
            onClick={() => {
              if (record.id !== undefined) {
                setSelectedUserId(record.id);
                setIsModalVisible(true);
                setIsNavbarVisible(false);
              } else {
                apii.error({ message: "Error", description: "Invalid user ID." });
              }
            }}
          >
            Update Role
          </Button>
          <Popconfirm
            title="Delete the User"
            okText="Yes"
            cancelText="No"
            onConfirm={() => {
              if (record.id !== undefined) {
                handleDelete(record.id);
              } else {
                apii.error({ message: "Error", description: "Invalid user ID." });
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
          <Input value={searchReload} onChange={onSearchInputChange} className="mr-2" placeholder="Search by name" />
          <Button type="primary" icon={<SearchOutlined />} onClick={handleSearch}>
            Search
          </Button>
        </div>
      </div>

      
      <Table columns={columns} dataSource={listUser} pagination={false} rowKey="id" />
      <Pagination
        current={pageNow}
        total={totalElements}
        pageSize={pageSize}
        onChange={onPaginationChange}
        style={{ textAlign: 'end', marginTop: '20px' }}
      />
      <Modal title="Update Roles" visible={isModalVisible} onCancel={handleModalCancel} footer={null}>
        <Form onFinish={handleSubmit}>
          <Form.Item>
            <Checkbox indeterminate={indeterminate} onChange={onCheckAllChange} checked={checkAll}>
              Check all
            </Checkbox>
            <Divider />
            <Checkbox.Group
              options={plainOptions
                .filter(option => option.id !== undefined)  // Filter out undefined IDs
                .map(option => ({ label: option.name, value: option.id as number }))}  // Use the correct field, e.g., name
              value={checkedList}
              onChange={onChange}
            />
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

export default UserCollection;
