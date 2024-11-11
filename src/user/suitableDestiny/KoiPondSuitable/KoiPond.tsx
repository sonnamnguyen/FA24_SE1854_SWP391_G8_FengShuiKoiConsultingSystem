import React, { useEffect, useState, useCallback } from "react";
import { Card, Col, Row, Pagination, Modal, Carousel, Spin } from "antd";
import ShelterCategory from "../../../models/ShelterCategory";
import { findByShelterCategoryDestiny } from "../../../admin/api/ShelterCategoryAPI";
import api from "../../../axious/axious";

interface UserDetails {
  dob: string;
}

const KoiPond: React.FC = () => {
  const [userDetails, setUserDetails] = useState<UserDetails | null>(null);
  const [listDestiny, setListDestiny] = useState<string[]>([]);
  const [listShelterCategory, setListShelterCategory] = useState<ShelterCategory[]>([]);
  const [totalElements, setTotalElements] = useState(0);
  const [pageNow, setPageNow] = useState(1);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [selectedShelter, setSelectedShelter] = useState<ShelterCategory | null>(null);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const pageSize = 10;

  // Fetch user details
  const getUserDetails = async () => {
    try {
      setLoading(true);
      const response = await api.get("/users/my-info");
      if (response.data.code === 1000) {
        setUserDetails(response.data.result);
      } else {
        setError("Failed to retrieve user details.");
      }
    } catch (error) {
      console.error("Error fetching user details:", error);
      setError("Error fetching user details.");
    } finally {
      setLoading(false);
    }
  };

  // Fetch destiny details based on user's year of birth
  const getDestiny = useCallback(async () => {
    if (!userDetails?.dob) return;
    try {
      setLoading(true);
      const year = new Date(userDetails.dob).getFullYear();
      const response = await api.get(`/destinys/autoConsultation/${year}`);
      console.log("Destiny API Response: ", response.data); // Log the response for debugging
      if (response.data.code !== 1000) {
        throw new Error(`Error: ${response.data.message}`);
      }

      const destinyTuongSinhs = response.data.result?.destinyTuongSinh;
      const destiny = response.data.result?.destiny;

      if (destinyTuongSinhs || destiny) {
        const combinedDestiny = [
          ...(destinyTuongSinhs ? destinyTuongSinhs.split(',') : []),
          ...(destiny ? destiny.split(',') : []),
        ];
        setListDestiny(combinedDestiny);
      } else {
        console.warn("Destiny data is missing or undefined.");
        setError("Destiny data is unavailable.");
      }
    } catch (error) {
      console.error("Error fetching destiny:", error);
      setError("Could not retrieve destiny information.");
    } finally {
      setLoading(false);
    }
  }, [userDetails]);

  useEffect(() => {
    getUserDetails();
  }, []);

  // Fetch destiny when userDetails is updated
  useEffect(() => {
    if (userDetails?.dob) {
      getDestiny();
    }
  }, [userDetails, getDestiny]);

  // Fetch shelters based on destiny list
  useEffect(() => {
    if (listDestiny.length > 0) {
      setLoading(true);
      findByShelterCategoryDestiny(listDestiny, pageNow, pageSize)
        .then((shelterData) => {
          if (shelterData) {
            setListShelterCategory(shelterData.result);
            setTotalElements(shelterData.totalElements);
          } else {
            setError("No data found.");
          }
        })
        .catch((error) => {
          console.error("Error fetching shelters:", error);
          setError("Failed to load shelters.");
        })
        .finally(() => setLoading(false));
    }
  }, [listDestiny, pageNow]);

  const handleShelterClick = (shelter: ShelterCategory) => {
    setSelectedShelter(shelter);
    setIsModalVisible(true);
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
    setSelectedShelter(null);
  };

  const onPaginationChange = (page: number) => {
    setPageNow(page);
  };

  return (
    <>
      {error && (
        <div style={{ color: "red", textAlign: "center" }}>
          <p>{error}</p>
          <button onClick={getUserDetails}>Retry</button>
        </div>
      )}
      {loading ? (
        <Spin style={{ display: "block", margin: "auto" }} />
      ) : (
        <>
          <Row gutter={[24, 24]} justify="center" style={{ padding: "20px" }}>
            {listShelterCategory.length === 0 ? (
              <div>No shelters available</div>
            ) : (
              listShelterCategory.map((shelter, index) => (
                <Col xs={24} sm={12} md={8} key={index}>
                  <Card
                    hoverable
                    cover={
                      <img
                        alt={shelter.shelterCategoryName}
                        src={shelter.shelterImages?.[0]?.imageUrl}  // Using optional chaining
                        style={{
                          border: "2px solid red",
                          padding: "10px",
                          borderRadius: "8px",
                        }}
                      />
                    }
                    style={{
                      border: "1px solid #f0f0f0",
                      borderRadius: "8px",
                      textAlign: "center",
                    }}
                    onClick={() => handleShelterClick(shelter)}
                  >
                    <Card.Meta
                      title={<h3 style={{ margin: "10px 0" }}>{shelter.shelterCategoryName}</h3>}
                      description={<p style={{ margin: "0", color: "#666" }}>{shelter.description}</p>}
                    />
                  </Card>
                </Col>
              ))
            )}
          </Row>

          <Pagination
            current={pageNow}
            total={totalElements}
            pageSize={pageSize}
            onChange={onPaginationChange}
            style={{ textAlign: "center", marginTop: "20px" }}
          />

          <Modal
            title={"Shelter Details"}
            open={isModalVisible}
            onOk={handleModalCancel}
            onCancel={handleModalCancel}
            width={1000}
            style={{ top: '15%' }}
          >
            {selectedShelter && (
              <div style={{ display: "flex", gap: "20px" }}>
                <div style={{ flex: 1, maxHeight: "400px", overflow: "hidden" }}>
                  <Carousel autoplay>
                  {selectedShelter?.shelterImages?.length ? (
                  selectedShelter.shelterImages.map((image, index) => (
                        <div
                          key={index}
                          style={{
                            textAlign: "center",
                            display: "flex",
                            justifyContent: "center",
                          }}
                        >
                          <img
                            alt={selectedShelter.shelterCategoryName}
                            src={image.imageUrl}
                            style={{
                              maxHeight: "400px",
                              width: "100%",
                              objectFit: "contain",
                            }}
                          />
                        </div>
                      ))
                    ) : (
                      <p>No images available.</p>
                    )}
                  </Carousel>
                </div>
                <div style={{ flex: 1, padding: "0 20px" }}>
                  <p><strong>Description:</strong> {selectedShelter.description}</p>
                  <p><strong>Status:</strong> {selectedShelter.status}</p>
                </div>
              </div>
            )}
          </Modal>
        </>
      )}
    </>
  );
};

export default KoiPond;
