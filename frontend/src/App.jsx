import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import Navbar from './components/Navbar'
import RoleRoute from './components/RoleRoute'
import HomePage from './pages/HomePage'
import LoginPage from './pages/auth/LoginPage'
import RegisterPage from './pages/auth/RegisterPage'
import StudentDashboard from './pages/student/StudentDashboard'
import DriveDetailPage from './pages/student/DriveDetailPage'
import MyApplicationsPage from './pages/student/MyApplicationsPage'
import ProfilePage from './pages/student/ProfilePage'
import RecruiterDashboard from './pages/recruiter/RecruiterDashboard'
import DriveFormPage from './pages/recruiter/DriveFormPage'
import RecruiterDriveDetailPage from './pages/recruiter/RecruiterDriveDetailPage'
import CompanyProfilePage from './pages/recruiter/CompanyProfilePage'
import NotFoundPage from './pages/NotFoundPage'

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <div className="min-h-screen">
          <Navbar />
          <main>
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />

              <Route
                path="/student"
                element={
                  <RoleRoute role="STUDENT">
                    <StudentDashboard />
                  </RoleRoute>
                }
              />
              <Route
                path="/student/drives/:id"
                element={
                  <RoleRoute role="STUDENT">
                    <DriveDetailPage />
                  </RoleRoute>
                }
              />
              <Route
                path="/student/applications"
                element={
                  <RoleRoute role="STUDENT">
                    <MyApplicationsPage />
                  </RoleRoute>
                }
              />
              <Route
                path="/student/profile"
                element={
                  <RoleRoute role="STUDENT">
                    <ProfilePage />
                  </RoleRoute>
                }
              />

              <Route
                path="/recruiter"
                element={
                  <RoleRoute role="RECRUITER">
                    <RecruiterDashboard />
                  </RoleRoute>
                }
              />
              <Route
                path="/recruiter/drives/new"
                element={
                  <RoleRoute role="RECRUITER">
                    <DriveFormPage />
                  </RoleRoute>
                }
              />
              <Route
                path="/recruiter/drives/:id"
                element={
                  <RoleRoute role="RECRUITER">
                    <RecruiterDriveDetailPage />
                  </RoleRoute>
                }
              />
              <Route
                path="/recruiter/drives/:id/edit"
                element={
                  <RoleRoute role="RECRUITER">
                    <DriveFormPage />
                  </RoleRoute>
                }
              />
              <Route
                path="/recruiter/company"
                element={
                  <RoleRoute role="RECRUITER">
                    <CompanyProfilePage />
                  </RoleRoute>
                }
              />

              <Route path="*" element={<NotFoundPage />} />
            </Routes>
          </main>
        </div>
      </BrowserRouter>
    </AuthProvider>
  )
}
