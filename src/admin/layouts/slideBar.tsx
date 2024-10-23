import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Menu } from 'antd';
import { MailOutlined, SettingOutlined, AppstoreOutlined } from '@ant-design/icons';
import '../../css/SidebarAdmin.css';  // Importing the CSS file

const SidebarAdmin: React.FC = () => {
  const [openKeys, setOpenKeys] = useState<string[]>([]);

  const handleOpenChange = (keys: string[]) => {
    setOpenKeys(keys);
  };

  return (
    <div className="sidebar pe-4 pb-3">
      <nav className="navbar bg-light navbar-light">
        <Link to="/" className="navbar-brand mx-4 mb-3">
          <h3 className="text-primary">
            <i className="fa me-2"></i>DASHMIN
          </h3>
        </Link>
        <div className="d-flex align-items-center ms-4 mb-4">
          <div className="position-relative">
            <img
              className="rounded-circle"
              src="img/user.jpg"
              alt=""
              style={{ width: '40px', height: '40px' }}
            />
            <div className="bg-success rounded-circle border border-2 border-white position-absolute end-0 bottom-0 p-1"></div>
          </div>
          <div className="ms-3">
            <h6 className="mb-0">John Doe</h6>
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
            <Link to="/">Dashboard</Link>
          </Menu.Item>

          <Menu.SubMenu key="sub1" icon={<MailOutlined />} title="KOI FISH ANIMAL">
            <Menu.Item key="2">
              <Link to="/add-koi">ADD KOI FISH</Link>
            </Menu.Item>
            <Menu.Item key="3">
              <Link to="/update-koi">UPDATE KOI FISH</Link>
            </Menu.Item>
            <Menu.Item key="4">
              <Link to="/view-koi">VIEW KOI FISH</Link>
            </Menu.Item>
          </Menu.SubMenu>

          <Menu.SubMenu key="sub2" icon={<SettingOutlined />} title="KOI FISH COLOR">
            <Menu.Item key="5">
              <Link to="/add-color">ADD COLOR KOI FISH</Link>
            </Menu.Item>
            <Menu.Item key="6">
              <Link to="/update-color">UPDATE COLOR KOI FISH</Link>
            </Menu.Item>
            <Menu.Item key="7">
              <Link to="/view-color">VIEW COLOR KOI FISH</Link>
            </Menu.Item>
          </Menu.SubMenu>

          <Menu.SubMenu key="sub3" icon={<AppstoreOutlined />} title="PONDS KOI FISH">
            <Menu.Item key="8">
              <Link to="/add-ponds">ADD PONDS</Link>
            </Menu.Item>
            <Menu.Item key="9">
              <Link to="/update-ponds">UPDATE PONDS</Link>
            </Menu.Item>
            <Menu.Item key="10">
              <Link to="/view-ponds">VIEW PONDS</Link>
            </Menu.Item>
          </Menu.SubMenu>

          <Menu.SubMenu key="sub4" icon={<AppstoreOutlined />} title="PONDS SHAPE KOI">
            <Menu.Item key="11">
              <Link to="/add-shape">ADD SHAPE PONDS</Link>
            </Menu.Item>
            <Menu.Item key="12">
              <Link to="/update-shape">UPDATE SHAPE PONDS</Link>
            </Menu.Item>
            <Menu.Item key="13">
              <Link to="/view-shape">VIEW SHAPE PONDS</Link>
            </Menu.Item>
          </Menu.SubMenu>
        </Menu>
      </nav>
    </div>
  );
};

export default SidebarAdmin;
