import { useEffect, useState } from 'react'
import api from '../../lib/api'
import DriveCard from '../../components/DriveCard'
import Spinner from '../../components/Spinner'
import Alert from '../../components/Alert'

export default function StudentDashboard() {
  const [drives, setDrives] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    api
      .get('/drives')
      .then((res) => setDrives(res.data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="mx-auto max-w-6xl px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900">Open hiring drives</h1>
      <p className="mt-1 text-sm text-gray-500">
        Browse drives published by recruiters and apply before the deadline.
      </p>

      {error && (
        <div className="mt-4">
          <Alert type="error">{error}</Alert>
        </div>
      )}
      {loading && <Spinner />}

      {!loading && !error && drives.length === 0 && (
        <div className="mt-8 rounded-lg border border-dashed border-gray-300 p-10 text-center text-sm text-gray-500">
          No drives are open right now. Check back later.
        </div>
      )}

      <div className="mt-6 grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {drives.map((drive) => (
          <DriveCard key={drive.id} drive={drive} />
        ))}
      </div>
    </div>
  )
}
