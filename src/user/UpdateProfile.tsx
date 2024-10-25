import React, { useEffect, useState } from "react";
import { Modal, Button } from "react-bootstrap";
import { getToken, setToken } from "../service/localStorageService";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

function UpdateProfile() {
  const [userName, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [fullName, setFullName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [password, setPassword] = useState("");
  const [passwordAgain, setPasswordAgain] = useState("");
  const [gender, setGender] = useState("");
  const [dob, setDOB] = useState("");
  const [avatar, setAvatar] = useState<File | null>(null);

  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const [modalNotification, setModalNotification] = useState("");
  const [modalNotificationColor, setModalNotificationColor] = useState("red");

  const [errorUserName, setErrorUserName] = useState("");
  const [errorEmail, setErrorEmail] = useState("");
  const [errorPhoneNumber, setErrorPhoneNumber] = useState("");
  const [errorDOB, setErrorDOB] = useState("");
  const [errorPassword, setErrorPassword] = useState("");
  const [errorPasswordAgain, setErrorPasswordAgain] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const token = getToken();
    if (token) {
      const userData = jwtDecode(token);
      if (userData) {
        setUserName(userData.sub || "");
      }
    }
  }, []);

  const checkPassword = (password: string) => {
    const passwordRegex = /^(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{4,}$/;
    if (!passwordRegex.test(password)) {
      setErrorPassword(
        "Password must be at least 4 characters and include at least 1 special character (!@#$%^&*)"
      );
      return false;
    } else {
      setErrorPassword("");
      return true;
    }
  };

  const checkEmail = (email: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setErrorEmail("Please enter a valid email address");
      return false;
    } else {
      setErrorEmail("");
      return true;
    }
  };

  const checkPhoneNumber = (phoneNumber: string) => {
    const phoneRegex = /^\d{10}$/;
    if (!phoneRegex.test(phoneNumber)) {
      setErrorPhoneNumber("Phone number must be 10 digits");
      return false;
    } else {
      setErrorPhoneNumber("");
      return true;
    }
  };

  const checkDOB = (dob: string) => {
    if (!dob) {
      setErrorDOB("Date of birth is required");
      return false;
    }
    const today = new Date();
    const birthDate = new Date(dob);
    const age = today.getFullYear() - birthDate.getFullYear();

    if (age < 18) {
      setErrorDOB("You must be at least 18 years old");
      return false;
    } else {
      setErrorDOB("");
      return true;
    }
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setPassword(value);
    setErrorPassword("");
    checkPassword(value);
  };

  const emailExits = async (email: string) => {
    const endpoint = `http://localhost:9090/users/existByEmail?email=${email}`;
    try {
      const response = await fetch(endpoint);
      const data = await response.text();
      if (data === "true") {
        setErrorEmail("Email already exists!");
        return false;
      }
      return true;
    } catch (error) {
      console.error("Error checking email:", error);
      return false;
    }
  };

  const handleEmail = async (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
    setErrorEmail("");
    return emailExits(e.target.value);
  };

  const userNameExits = async (name: string) => {
    const endpoint = `http://localhost:9090/users/existByUserName?userName=${name}`;
    try {
      const response = await fetch(endpoint);
      const data = await response.text();
      if (data === "true") {
        setErrorUserName("Username already exists!");
        return false;
      }
      return true;
    } catch (error) {
      console.error("Error checking username:", error);
      return false;
    }
  };

  const handleUserName = async (e: React.ChangeEvent<HTMLInputElement>) => {
    setUserName(e.target.value);
    setErrorUserName("");
    return userNameExits(e.target.value);
  };

  const checkPasswordAgain = (password: string, passwordAgainValue: string) => {
    if (passwordAgainValue !== password) {
      setErrorPasswordAgain("Passwords do not match.");
      return false;
    } else {
      setErrorPasswordAgain("");
      return true;
    }
  };

  const handlePasswordAgain = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setPasswordAgain(value);
    setErrorPasswordAgain("");
    checkPasswordAgain(password, value);
  };

  const handleAvatarChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setAvatar(e.target.files[0]);
    }
  };

  const getBase64 = (file: File): Promise<string | null> => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () =>
        resolve(reader.result ? (reader.result as string) : null);
      reader.onerror = (error) => reject(error);
    });
  };
  const handleHome = () => {
    navigate("/");
  };

  const handleRefreshToken = async () => {
    const refreshToken = getToken(); // Get refresh token from local storage or cookie
    if (!refreshToken) {
      console.error("No refresh token found");
      navigate("/login"); // Navigate to login if no token
      return null;
    }
    try {
      const response = await fetch("http://localhost:9090/auth/refresh", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ token: refreshToken }),
      });
      if (response.ok) {
        const data = await response.json();
        if (data.code === 1000) {
          setToken(data.result.token); // Save new access token
          return true; // Indicate success
        } else {
          console.error("Failed to refresh token");
          navigate("/login");
        }
      } else {
        console.error("Failed to refresh token");
        navigate("/login");
      }
    } catch (error) {
      console.error("Error refreshing token:", error);
      navigate("/login");
    }
    return null; // Indicate failure
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorUserName("");
    setErrorEmail("");
    setErrorPhoneNumber("");
    setErrorPassword("");
    setErrorPasswordAgain("");
    setErrorDOB("");

    const isUserNameValid = await userNameExits(userName);
    const isEmailValid = await emailExits(email);
    const isPasswordValid = checkPassword(password);
    const isPasswordAgainValid = checkPasswordAgain(password, passwordAgain);
    const isEmailCharacterValid = checkEmail(email);
    const isPhoneNumberValid = checkPhoneNumber(phoneNumber);
    const isDOBValid = checkDOB(dob);

    if (
      isUserNameValid &&
      isEmailValid &&
      isPasswordValid &&
      isPasswordAgainValid &&
      isEmailCharacterValid &&
      isPhoneNumberValid &&
      isDOBValid
    ) {
      // Convert avatar to Base64 before sending
      const base64Avatar = avatar ? await getBase64(avatar) : null;

      const tokenRefreshed = await handleRefreshToken(); // Call refresh token logic

      if (tokenRefreshed) {
        try {
          const response = await fetch(
            `http://localhost:9090/users/${userName}`,
            {
              method: "PUT",
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${getToken()}`,
              },
              body: JSON.stringify({
                userName,
                password,
                email,
                dob,
                phoneNumber,
                gender,
                avatar: base64Avatar, // Pass avatar if available
              }),
            }
          );

          const data = await response.json();
          if (data.code !== 1000) {
            setModalMessage("An error occurred during the update.");
            setModalNotification("Update failed");
            setModalNotificationColor("red");
            setShowModal(true);
          } else {
            setModalMessage("Profile updated successfully!");
            setModalNotification("Success");
            setModalNotificationColor("green");
            setShowModal(true);
          }
        } catch (error) {
          console.error("Error updating profile:", error);
        }
      }
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        {/* Form Fields */}
        <div>
          <label>Username</label>
          <input
            type="text"
            value={userName}
            onChange={handleUserName}
            required
          />
          {errorUserName && <p style={{ color: "red" }}>{errorUserName}</p>}
        </div>

        <div>
          <label>Email</label>
          <input type="email" value={email} onChange={handleEmail} required />
          {errorEmail && <p style={{ color: "red" }}>{errorEmail}</p>}
        </div>

        <div>
          <label>Full Name</label>
          <input
            type="text"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            required
          />
        </div>

        <div>
          <label>Phone Number</label>
          <input
            type="text"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            required
          />
          {errorPhoneNumber && (
            <p style={{ color: "red" }}>{errorPhoneNumber}</p>
          )}
        </div>

        <div>
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={handlePasswordChange}
            required
          />
          {errorPassword && <p style={{ color: "red" }}>{errorPassword}</p>}
        </div>

        <div>
          <label>Confirm Password</label>
          <input
            type="password"
            value={passwordAgain}
            onChange={handlePasswordAgain}
            required
          />
          {errorPasswordAgain && (
            <p style={{ color: "red" }}>{errorPasswordAgain}</p>
          )}
        </div>

        <div>
          <label>Gender</label>
          <select value={gender} onChange={(e) => setGender(e.target.value)}>
            <option value="">Select Gender</option>
            <option value="male">Male</option>
            <option value="female">Female</option>
            <option value="other">Other</option>
          </select>
        </div>

        <div>
          <label>Date of Birth</label>
          <input
            type="date"
            value={dob}
            onChange={(e) => setDOB(e.target.value)}
            required
          />
          {errorDOB && <p style={{ color: "red" }}>{errorDOB}</p>}
        </div>

        {/* Avatar Upload */}
        <div>
          <label>Avatar</label>
          <input type="file" onChange={handleAvatarChange} />
        </div>

        <button type="submit">Update Profile</button>
      </form>

      {/* Modal */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{modalNotification}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p style={{ color: modalNotificationColor }}>{modalMessage}</p>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={() => setShowModal(false)}>Close</Button>
          <Button onClick={handleHome}>Return to Home</Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default UpdateProfile;
