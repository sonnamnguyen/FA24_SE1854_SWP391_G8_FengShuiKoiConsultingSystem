import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "../components/Login";
import Authenticate from "../components/Authenticate";
import Blog from "../homepage/Blog";
import ActivateAccount from "../user/ActivateAccount";
import ViewProfile from "../user/ViewProfile";
import Register from "../user/Register";
import Page403 from "../utils/Page403";
import Page404 from "../utils/Page404";
import ForgotPassword from "../user/ForgotPassword";
import ResetPassword from "../user/ResetPassword";
import UpdateProfile from "../user/UpdateProfile";
import AdminRoute from "../admin/AdminRoute";
import AnimalCollection from "../admin/AnimalCollection";
import AdminPage from "../admin/layouts/AdminPage";
import AddKoiFish from "../admin/animal/AddKoiFish";
import ViewAdd from "../admin/animal/layoutAnimal/ViewAdd";
import ViewManagement from "../admin/animal/layoutAnimal/ViewManagement";
import ViewAddShelter from "../admin/shelter/layoutShelter/ViewAddShelter";
import ShelterViewAdmin from "../admin/ShelterCollection";
import ViewManagementShelter from "../admin/shelter/layoutShelter/ViewManagementShelter";
import ViewAddColor from "../admin/color/layoutCokor/ViewAddColor";
import ViewAddShape from "../admin/shape/layoutShape/ViewAddShape";
import ViewColorManagement from "../admin/color/layoutCokor/ViewColorManagement";
import ViewShapeManagement from "../admin/shape/layoutShape/ViewManagementShape";
import ViewPost from "../user/ViewPost";
import MyPostList from "../user/MyPostList";
import UpdatePost from "../user/UpdatePost";
import CreatePost from "../user/CreatePost";
import Postlist from "../user/Postlist";
import ViewMyPost from "../user/ViewMyPost";
import VNPAY from "../user/VNPAY";
import AboutUs from "../user/AboutUs";
import ViewUserManagement from "../admin/user/UserManagement";
import AutoConsultation from "../user/autoConsultation/AutoConsultationView";
import CompatibilityCaculation from "../user/compatibilityCaculation/CompatibilityCaculationView";
import LoginSignUp from "../components/LoginNew";

// Consultation - user
import ConsultationRequest from "../user/consultation/request/ConsultationRequestView";
import PaymentPage from "../user/payment/PaymentPageView";
import BillPage from "../user/bill/BillPageView";
import PaymentSuccessPage from "../user/payment/PaymentSuccessPageView";
import ConsultationRequestDetail from "../user/consultation/request/ConsultationRequestDetailView";

// Consultation - admin
import ViewConsultationRequest from "../admin/consultation/ViewConsultationRequest";
import ViewConsultationRequestDetail from "../admin/consultation/ViewConsultationRequestDetail";
import ViewConsultationResult from "../admin/consultation/ViewConsultationResult";
import ViewConsultationAnimal from "../admin/consultation/ViewConsultationAnimal";
import ViewConsultationShelter from "../admin/consultation/ViewConsultationShelter";

// Bill - Payment
import ViewBill from "../admin/transaction/ViewBill";
import ViewPayment from "../admin/transaction/ViewPayment";
import UserRoute from "../user/UserRoute/UserRoute";
import KoiFish from "../user/suitableDestiny/KoiFishSuitable/KoiFish";
import KoiPond from "../user/suitableDestiny/KoiPondSuitable/KoiPond";

const AppRoutes = () => {
  // const ProtectedBlog = AdminRoute(Blog);
  const Page = AdminRoute(AdminPage);
  const PageViewAddKoi = AdminRoute(ViewAdd);
  const PageViewManagementKoi = AdminRoute(ViewManagement);
  const PageViewAddPond = AdminRoute(ViewAddShelter);
  const PageViewAddColor = AdminRoute(ViewAddColor);
  const PageViewAddShape = AdminRoute(ViewAddShape);
  const PageViewColor = AdminRoute(ViewColorManagement);
  const PageViewShape = AdminRoute(ViewShapeManagement);
  const PageViewUser = AdminRoute(ViewUserManagement);

  {
    /* Admin Consultation */
  }
  const PageViewConsultationRequest = AdminRoute(ViewConsultationRequest);
  const PageViewConsultationRequestDetail = AdminRoute(
    ViewConsultationRequestDetail
  );
  const PageViewConsultationResult = AdminRoute(ViewConsultationResult);
  const PageViewConsultationAnimal = AdminRoute(ViewConsultationAnimal);
  const PageViewConsultationShelter = AdminRoute(ViewConsultationShelter);

  const PageViewBill = AdminRoute(ViewBill);
  const PageViewPayment = AdminRoute(ViewPayment);
//route user

const UserKoiFish = UserRoute(KoiFish);
const UserPondFish = UserRoute(KoiPond);

  return (
    <Router>
      <Routes>
      <Route path="/koi-fishs" element={<UserKoiFish />} />
      <Route path="/koi-ponds" element={<UserPondFish />} />
        <Route path="/my-post" element={<ViewMyPost />} />
        <Route path="/create-post" element={<CreatePost />} />
        <Route path="/posts" element={<ViewPost />} />
        <Route path="/about-us" element={<AboutUs />} />
        <Route path="/vnpay-success" element={<VNPAY />} />
        <Route path="/posts/:id" element={<ViewPost />} />
        <Route path="/post-list" element={<Postlist />} />
        <Route path="/update-post/:id" element={<UpdatePost />} />
        <Route path="/my-post-list" element={<MyPostList />} />
        <Route path="/login" element={<Login />} />
        <Route path="/view-profile" element={<ViewProfile />} />
        <Route path="/register" element={<Register />} />
        <Route path="/update-profile" element={<UpdateProfile />} />
        <Route path="/authenticate" element={<Authenticate />} />
        <Route path="/" element={<Blog />} />
        <Route path="/403" element={<Page403 />} />
        <Route path="*" element={<Page404 />} />
        <Route path="/activate/:email/:code" element={<ActivateAccount />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password/:email" element={<ResetPassword />} />
        <Route path="/view-koi" element={<PageViewManagementKoi />} />
        <Route path="/add-koi" element={<PageViewAddKoi />} />
        <Route path="/view-ponds" element={<ViewManagementShelter />} />
        <Route path="/add-ponds" element={<PageViewAddPond />} />
        <Route path="/admin-page" element={<Page />} />
        <Route path="/add-color" element={<PageViewAddColor />} />
        <Route path="/add-shape" element={<PageViewAddShape />} />
        <Route path="/view-color" element={<PageViewColor />} />
        <Route path="/view-shape" element={<PageViewShape />} />
        <Route path="/auto-consultation" element={<AutoConsultation />} />
        <Route
          path="/compatibility_caculation"
          element={<CompatibilityCaculation />}
        />

        <Route path="/view-user" element={<PageViewUser />} />

        {/* Consultation */}
        {/* Consultation Request */}
        <Route path="/consultation-request" element={<ConsultationRequest />} />
        {/* Thanh toán Consultation Request */}
        <Route
          path="/consultation-request/:requestId/payment"
          element={<PaymentPage />}
        />
        {/* In Bill */}
        <Route path="/bill/:billId" element={<BillPage />} />
        {/* payment-success */}
        <Route path="/payment-success" element={<PaymentSuccessPage />} />
        {/* Consultation Request Detail */}
        <Route
          path="/consultation-request-detail"
          element={<ConsultationRequestDetail />}
        />

        {/* Admin Consultation */}
        <Route
          path="/view-consultation-request"
          element={<PageViewConsultationRequest />}
        />
        <Route
          path="/view-consultation-request-detail"
          element={<PageViewConsultationRequestDetail />}
        />
        <Route
          path="/view-consultation-result"
          element={<PageViewConsultationResult />}
        />
        <Route
          path="/view-consultation-animal"
          element={<PageViewConsultationAnimal />}
        />
        <Route
          path="/view-consultation-shelter"
          element={<PageViewConsultationShelter />}
        />

        {/* Bill - Payment*/}
        <Route path="/view-bills" element={<PageViewBill />} />
        <Route path="/view-payments" element={<PageViewPayment />} />
      </Routes>
    </Router>
  );
};

export default AppRoutes;
