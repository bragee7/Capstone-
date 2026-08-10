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

export default function RecruiterDashboard() {
  const [drives, setDrives] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const load = () => {
    api
      .get('/recruiter/drives')
      .then((res) => setDrives(res.data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false))
  }

  useEffect(load, [])

  const publish = async (drive) => {
    try {
      const res = await api.patch(`/recruiter/drives/${drive.id}/publish`)
      setDrives((list) => list.map((d) => (d.id === drive.id ? res.data : d)))
    } catch (err) {
      setError(err.message)
    }
  }

  const close = async (drive) => {
    try {
      const res = await api.patch(`/recruiter/drives/${drive.id}/close`)
      setDrives((list) => list.map((d) => (d.id === drive.id ? res.data : d)))
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
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">My hiring drives</h1>
          <p className="mt-1 text-sm text-gray-500">Create, publish, and manage drives for your company.</p>
        </div>
        <Link
          to="/recruiter/drives/new"
          className="rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white hover:bg-indigo-700"
        >
          New drive
        </Link>
      </div>

      {drives.length === 0 && (
        <div className="mt-8 rounded-lg border border-dashed border-gray-300 p-10 text-center text-sm text-gray-500">
          You have not created any drives yet.
        </div>
      )}

      <div className="mt-6 space-y-4">
        {drives.map((drive) => (
          <div key={drive.id} className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
            <div className="flex items-start justify-between gap-3">
              <div>
                <h3 className="text-base font-semibold text-gray-900">{drive.title}</h3>
                <p className="mt-0.5 text-sm text-gray-500">
                  {drive.location || '—'} · {drive.applicationCount} application(s) · Deadline {formatDate(drive.applicationDeadline)}
                </p>
              </div>
              <StatusBadge status={drive.status} />
            </div>
            <div className="mt-4 flex flex-wrap gap-2">
              <Link
                to={`/recruiter/drives/${drive.id}`}
                className="rounded-lg bg-indigo-50 px-3 py-1.5 text-sm font-medium text-indigo-700 hover:bg-indigo-100"
              >
                View applications
              </Link>
              <Link
                to={`/recruiter/drives/${drive.id}/edit`}
                className="rounded-lg border border-gray-300 px-3 py-1.5 text-sm font-medium text-gray-700 hover:bg-gray-50"
              >
                Edit
              </Link>
              {drive.status === 'DRAFT' && (
                <button
                  onClick={() => publish(drive)}
                  className="rounded-lg bg-green-600 px-3 py-1.5 text-sm font-medium text-white hover:bg-green-700"
                >
                  Publish
                </button>
              )}
              {drive.status === 'PUBLISHED' && (
                <button
                  onClick={() => close(drive)}
                  className="rounded-lg border border-red-200 px-3 py-1.5 text-sm font-medium text-red-600 hover:bg-red-50"
                >
                  Close
                </button>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
