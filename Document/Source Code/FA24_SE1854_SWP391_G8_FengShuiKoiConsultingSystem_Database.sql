CREATE DATABASE FA24_SE1854_SWP391_G8_FengShuiKoiConsultingSystem
USE FA24_SE1854_SWP391_G8_FengShuiKoiConsultingSystem


-- Bảng quản lý loại động vật (tổng quát hóa)
CREATE TABLE AnimalCategory (
    AnimalCategoryID INT PRIMARY KEY IDENTITY(1,1),
    CategoryName NVARCHAR(100) NOT NULL,
    Description NVARCHAR(255),
    Note NVARCHAR(255),
    isActive BIT,
    CreateDate DATETIME DEFAULT GETDATE(),
    CreateBy NVARCHAR(50),
    UpdateDate DATETIME,
    UpdateBy NVARCHAR(50)
);

-- Bảng quản lý động vật (tổng quát hóa)
CREATE TABLE Animal (
    AnimalID INT PRIMARY KEY IDENTITY(1,1),
    AnimalName NVARCHAR(100) NOT NULL,
    AnimalCategoryID INT FOREIGN KEY REFERENCES AnimalCategory(AnimalCategoryID),
    ElementID INT FOREIGN KEY REFERENCES Elements(ElementID), -- Phù hợp với bản mệnh
    Description NVARCHAR(255),
    ParentID INT,
    Note NVARCHAR(255),
    isActive BIT,
    CreateDate DATETIME DEFAULT GETDATE(),
    CreateBy NVARCHAR(50),
    UpdateDate DATETIME,
    UpdateBy NVARCHAR(50)
);

-- Bảng quản lý bản mệnh
CREATE TABLE Elements (
    ElementID INT PRIMARY KEY IDENTITY(1,1),
    ElementName NVARCHAR(50) NOT NULL,
    Description NVARCHAR(255),
    Note NVARCHAR(255),
    isActive BIT,
    CreateDate DATETIME DEFAULT GETDATE(),
    CreateBy NVARCHAR(50),
    UpdateDate DATETIME,
    UpdateBy NVARCHAR(50)
);

-- Bảng quản lý loại nơi trú ẩn
CREATE TABLE ShelterCategory (
    ShelterCategoryID INT PRIMARY KEY IDENTITY(1,1),
    CategoryName NVARCHAR(100) NOT NULL,
    Description NVARCHAR(255),
    Note NVARCHAR(255),
    isActive BIT,
    CreateDate DATETIME DEFAULT GETDATE(),
    CreateBy NVARCHAR(50),
    UpdateDate DATETIME,
    UpdateBy NVARCHAR(50)
);

-- Bảng quản lý đặc điểm của nơi trú ẩn
CREATE TABLE ShelterFeatures (
    ShelterFeatureID INT PRIMARY KEY IDENTITY(1,1),
    ShelterCategoryID INT FOREIGN KEY REFERENCES ShelterCategory(ShelterCategoryID),
    FeatureName NVARCHAR(100) NOT NULL,
    Description NVARCHAR(255),
    Note NVARCHAR(255),
    isActive BIT,
    CreateDate DATETIME DEFAULT GETDATE(),
    CreateBy NVARCHAR(50),
    UpdateDate DATETIME,
    UpdateBy NVARCHAR(50)
);

-- Bảng quản lý thông tin tư vấn
CREATE TABLE Consultations (
    ConsultationID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT FOREIGN KEY REFERENCES Users(UserID),
    AnimalID INT FOREIGN KEY REFERENCES Animal(AnimalID),
    ShelterFeatureID INT FOREIGN KEY REFERENCES ShelterFeatures(ShelterFeatureID),
    Score INT,
    Note NVARCHAR(255),
    isActive BIT,
    CreateDate DATETIME DEFAULT GETDATE(),
    CreateBy NVARCHAR(50),
    UpdateDate DATETIME,
    UpdateBy NVARCHAR(50)
);

-- Bảng quản lý thông tin quảng cáo
CREATE TABLE Ads (
    AdID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT FOREIGN KEY REFERENCES Users(UserID),
    Title NVARCHAR(100) NOT NULL,
    Content NVARCHAR(MAX),
    ElementID INT FOREIGN KEY REFERENCES Elements(ElementID),
    AdPackageID INT FOREIGN KEY REFERENCES AdPackages(AdPackageID),
    Note NVARCHAR(255),
    isActive BIT,
    CreateDate DATETIME DEFAULT GETDATE(),
    CreateBy NVARCHAR(50),
    UpdateDate DATETIME,
    UpdateBy NVARCHAR(50)
);

-- Bảng quản lý thông tin gói quảng cáo
CREATE TABLE AdPackages (
    AdPackageID INT PRIMARY KEY IDENTITY(1,1),
    PackageName NVARCHAR(100) NOT NULL,
    Price DECIMAL(10, 2),
    DurationInDays INT,
    Note NVARCHAR(255),
    isActive BIT,
    CreateDate DATETIME DEFAULT GETDATE(),
    CreateBy NVARCHAR(50),
    UpdateDate DATETIME,
    UpdateBy NVARCHAR(50)
);

-- Bảng quản lý vai trò người dùng
CREATE TABLE UserRole (
    RoleID INT PRIMARY KEY IDENTITY(1,1),
    RoleName NVARCHAR(50) NOT NULL,
    Note NVARCHAR(255),
    isActive BIT,
    CreateDate DATETIME DEFAULT GETDATE(),
    CreateBy NVARCHAR(50),
    UpdateDate DATETIME,
    UpdateBy NVARCHAR(50)
);

-- Bảng quản lý người dùng
CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    Username NVARCHAR(100) UNIQUE NOT NULL,
    PasswordHash NVARCHAR(255) NOT NULL,
    FullName NVARCHAR(100),
    Gender NVARCHAR(10),
    DateOfBirth DATE,
    RoleID INT FOREIGN KEY REFERENCES UserRole(RoleID),
    ElementID INT FOREIGN KEY REFERENCES Elements(ElementID),
    Note NVARCHAR(255),
    isActive BIT,
    CreateDate DATETIME DEFAULT GETDATE(),
    CreateBy NVARCHAR(50),
    UpdateDate DATETIME,
    UpdateBy NVARCHAR(50)
);
