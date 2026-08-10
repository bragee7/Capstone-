import { Link, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

const navLinkClass = ({ isActive }) =>
  `rounded-lg px-3 py-2 text-sm font-medium transition-colors ${
    isActive ? 'bg-indigo-50 text-indigo-700' : 'text-gray-600 hover:bg-gray-100 hover:text-gray-900'
  }`

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const homePath = user?.role === 'STUDENT' ? '/student' : user?.role === 'RECRUITER' ? '/recruiter' : '/'

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <header className="border-b border-gray-200 bg-white">
      <div className="mx-auto flex h-16 max-w-6xl items-center justify-between px-4">
        <div className="flex items-center gap-8">
          <Link to={homePath} className="text-lg font-bold text-indigo-600">
            CampusHire
          </Link>
          {user && (
            <nav className="hidden items-center gap-1 sm:flex">
              {user.role === 'STUDENT' && (
                <>
                  <NavLink to="/student" end className={navLinkClass}>
                    Drives
                  </NavLink>
                  <NavLink to="/student/applications" className={navLinkClass}>
                    My Applications
                  </NavLink>
                  <NavLink to="/student/profile" className={navLinkClass}>
                    Profile
                  </NavLink>
                </>
              )}
              {user.role === 'RECRUITER' && (
                <>
                  <NavLink to="/recruiter" end className={navLinkClass}>
                    My Drives
                  </NavLink>
                  <NavLink to="/recruiter/drives/new" className={navLinkClass}>
                    New Drive
                  </NavLink>
                  <NavLink to="/recruiter/company" className={navLinkClass}>
                    Company
                  </NavLink>
                </>
              )}
            </nav>
          )}
        </div>
        <div className="flex items-center gap-3">
          {user ? (
            <>
              <div className="text-right">
                <p className="text-sm font-medium leading-tight text-gray-900">{user.name}</p>
                <p className="text-xs leading-tight text-gray-500">{user.role}</p>
              </div>
              <button
                onClick={handleLogout}
                className="rounded-lg border border-gray-300 px-3 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
              >
                Sign out
              </button>
            </>
          ) : (
            <>
              <Link
                to="/login"
                className="rounded-lg border border-gray-300 px-3 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
              >
                Sign in
              </Link>
              <Link
                to="/register"
                className="rounded-lg bg-indigo-600 px-3 py-2 text-sm font-medium text-white hover:bg-indigo-700"
              >
                Register
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  )
}
