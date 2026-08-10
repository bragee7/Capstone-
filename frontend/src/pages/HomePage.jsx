import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function HomePage() {
  const { user } = useAuth()

  if (user) {
    return <Navigate to={user.role === 'RECRUITER' ? '/recruiter' : '/student'} replace />
  }

  return (
    <div className="mx-auto max-w-3xl px-4 py-20 text-center">
      <h1 className="text-4xl font-bold text-gray-900">
        Internship &amp; Campus Hiring Platform
      </h1>
      <p className="mx-auto mt-4 max-w-xl text-gray-600">
        Students can browse hiring drives, complete their profile, and apply in one place. Recruiters can post
        drives, review applicants, and shortlist candidates — all in a single workflow.
      </p>
      <div className="mt-8 flex justify-center gap-4">
        <a
          href="/register"
          className="rounded-lg bg-indigo-600 px-6 py-3 text-sm font-medium text-white hover:bg-indigo-700"
        >
          Create an account
        </a>
        <a
          href="/login"
          className="rounded-lg border border-gray-300 px-6 py-3 text-sm font-medium text-gray-700 hover:bg-gray-50"
        >
          Sign in
        </a>
      </div>
    </div>
  )
}
