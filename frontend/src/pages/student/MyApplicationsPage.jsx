import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../../lib/api'
import Spinner from '../../components/Spinner'
import Alert from '../../components/Alert'
import StatusBadge from '../../components/StatusBadge'

function formatDate(value) {
  if (!value) return '—'
  return new Date(value).toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' })
}

export default function MyApplicationsPage() {
  const [applications, setApplications] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    api
      .get('/applications/my')
      .then((res) => setApplications(res.data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false))
  }, [])

  const withdraw = async (application) => {
    try {
      await api.delete(`/applications/${application.id}`)
      setApplications((apps) => apps.filter((a) => a.id !== application.id))
    } catch (err) {
      setError(err.message)
    }
  }

  if (loading) return <Spinner />
  if (error) return (
    <div className="mx-auto max-w-4xl px-4 py-8">
      <Alert type="error">{error}</Alert>
    </div>
  )

  return (
    <div className="mx-auto max-w-4xl px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900">My applications</h1>
      <p className="mt-1 text-sm text-gray-500">Track the status of your applications.</p>

      {applications.length === 0 && (
        <div className="mt-8 rounded-lg border border-dashed border-gray-300 p-10 text-center text-sm text-gray-500">
          You have not applied to any drives yet.{' '}
          <Link to="/student" className="font-medium text-indigo-600 hover:text-indigo-700">
            Browse drives
          </Link>
        </div>
      )}

      <div className="mt-6 space-y-4">
        {applications.map((app) => (
          <div key={app.id} className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
            <div className="flex items-start justify-between gap-3">
              <div>
                <Link
                  to={`/student/drives/${app.driveId}`}
                  className="text-base font-semibold text-gray-900 hover:text-indigo-600"
                >
                  {app.driveTitle}
                </Link>
                <p className="mt-0.5 text-sm text-gray-500">
                  {app.driveCompanyName} · Applied {formatDate(app.appliedAt)}
                </p>
              </div>
              <StatusBadge status={app.status} />
            </div>
            {app.coverMessage && (
              <p className="mt-3 rounded-lg bg-gray-50 px-3 py-2 text-sm text-gray-600">{app.coverMessage}</p>
            )}
            {app.status === 'APPLIED' && (
              <button
                onClick={() => withdraw(app)}
                className="mt-3 rounded-lg border border-gray-300 px-3 py-1.5 text-sm font-medium text-gray-700 hover:bg-gray-50"
              >
                Withdraw
              </button>
            )}
          </div>
        ))}
      </div>
    </div>
  )
}
