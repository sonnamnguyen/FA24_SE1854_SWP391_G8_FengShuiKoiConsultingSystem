import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Menu } from 'antd';
import { MailOutlined, SettingOutlined, AppstoreOutlined } from '@ant-design/icons';
import '../../css/SidebarAdmin.css';  // Importing the CSS file
import api from '../../axious/axious';
import User from '../../models/User';

const SidebarAdmin: React.FC = () => {
  const [openKeys, setOpenKeys] = useState<string[]>([]);
  const [userDetails, setUserDetails] = useState<User | null>(null); // Added userDetails state

  const handleOpenChange = (keys: string[]) => {
    setOpenKeys(keys);
  };
  useEffect(() => {
    const fetchUserDetails = async () => {
      try {
        const response = await api.get("/users/my-info");
        if (response.data.code !== 1000) {
          throw new Error(`Error! Code: ${response.data.message}`);
        }
        setUserDetails(response.data.result); // Set user details if fetched successfully
      } catch (error) {
        console.error("Error fetching user details:", error);
      }
    };

    fetchUserDetails();
  }, []);
  return (
    <div className="sidebar pe-4 pb-3">
      <nav className="navbar bg-light navbar-light">
        <Link to="/" className="navbar-brand mx-4 mb-3">
          <h3 className="text-primary">
            <i className="fa me-2"></i>FENGSHUIKOI
          </h3>
        </Link>
        <div className="d-flex align-items-center ms-4 mb-4">
          <div className="position-relative">
            <img
              className="rounded-circle"
              src={userDetails?.avatar}
              alt=""
              style={{ width: '40px', height: '40px' }}
            />
            <div className="bg-success rounded-circle border border-2 border-white position-absolute end-0 bottom-0 p-1"></div>
          </div>
          <div className="ms-3">
            <h6 className="mb-0">{userDetails?.username}</h6>
            <span>Admin</span>
          </div>
        </div>
        <Menu
          mode="inline"
          openKeys={openKeys}
          onOpenChange={handleOpenChange}
          style={{ width: 256 }}
          theme="light"
        >
          <Menu.Item key="1" icon={<AppstoreOutlined />}>
            <Link to="/dash-board">Dashboard</Link>
          </Menu.Item>

          <Menu.SubMenu key="sub1" icon={<MailOutlined />} title="USER MANAGEMENT">
            <Menu.Item key="4">
              <Link to="/view-user">VIEW USER</Link>
            </Menu.Item>
          </Menu.SubMenu>

          <Menu.SubMenu key="sub2" icon={<MailOutlined />} title="KOI FISH ANIMAL">
            <Menu.Item key="2">
              <Link to="/add-koi">ADD KOI FISH</Link>
            </Menu.Item>
            <Menu.Item key="4">
              <Link to="/view-koi">VIEW KOI FISH</Link>
            </Menu.Item>
          </Menu.SubMenu>

          <Menu.SubMenu key="sub3" icon={<SettingOutlined />} title="KOI FISH COLOR">
            <Menu.Item key="5">
              <Link to="/add-color">ADD COLOR KOI FISH</Link>
            </Menu.Item>
            <Menu.Item key="7">
              <Link to="/view-color">VIEW COLOR KOI FISH</Link>
            </Menu.Item>
          </Menu.SubMenu>

          <Menu.SubMenu key="sub4" icon={<AppstoreOutlined />} title="PONDS KOI FISH">
            <Menu.Item key="8">
              <Link to="/add-ponds">ADD PONDS</Link>
            </Menu.Item>
            <Menu.Item key="10">
              <Link to="/view-ponds">VIEW PONDS</Link>
            </Menu.Item>
          </Menu.SubMenu>

          <Menu.SubMenu key="sub5" icon={<AppstoreOutlined />} title="PONDS SHAPE KOI">
            <Menu.Item key="11">
              <Link to="/add-shape">ADD SHAPE PONDS</Link>
            </Menu.Item>
            <Menu.Item key="13">
              <Link to="/view-shape">VIEW SHAPE PONDS</Link>
            </Menu.Item>
          </Menu.SubMenu>

          <Menu.SubMenu key="sub6" icon={<AppstoreOutlined />} title="CONSULTATION">
            <Menu.Item key="14">
              <Link to="/view-consultation-request">REQUEST</Link>
            </Menu.Item>
            <Menu.Item key="15">
              <Link to="/view-consultation-request-detail">REQUEST DETAIL</Link>
            </Menu.Item>
            <Menu.Item key="16">
              <Link to="/view-consultation-result">RESULT</Link>
            </Menu.Item>
            <Menu.Item key="17">
              <Link to="/view-consultation-animal">ANIMAL</Link>
            </Menu.Item>
            <Menu.Item key="18">
              <Link to="/view-consultation-shelter">SHELTER</Link>
            </Menu.Item>
          </Menu.SubMenu>

          <Menu.SubMenu key="sub7" icon={<AppstoreOutlined />} title="TRANSACTION">
            <Menu.Item key="19">
              <Link to="/view-bills">BILL</Link>
            </Menu.Item>
          </Menu.SubMenu>
        </Menu>
      </nav>
    </div>
  );
};

export default SidebarAdmin;
