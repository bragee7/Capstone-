import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import api from '../../lib/api'
import Spinner from '../../components/Spinner'
import Alert from '../../components/Alert'
import StatusBadge from '../../components/StatusBadge'

const statusOptions = ['APPLIED', 'SHORTLISTED', 'INTERVIEW', 'SELECTED', 'REJECTED']

function formatDate(value) {
  if (!value) return '—'
  return new Date(value).toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' })
}

export default function RecruiterDriveDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [applications, setApplications] = useState([])
  const [driveTitle, setDriveTitle] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    Promise.all([api.get('/recruiter/drives'), api.get(`/recruiter/drives/${id}/applications`)])
      .then(([drivesRes, appsRes]) => {
        const drive = drivesRes.data.find((d) => d.id === Number(id))
        if (!drive) throw new Error('Drive not found.')
        setDriveTitle(drive.title)
        setApplications(appsRes.data)
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false))
  }, [id])

  const updateStatus = async (application, status) => {
    try {
      const res = await api.patch(`/recruiter/drives/${id}/applications/${application.id}`, { status })
      setApplications((apps) => apps.map((a) => (a.id === application.id ? res.data : a)))
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
      <button
        onClick={() => navigate('/recruiter')}
        className="mb-4 text-sm font-medium text-indigo-600 hover:text-indigo-700"
      >
        &larr; Back to my drives
      </button>

      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">{driveTitle}</h1>
          <p className="mt-1 text-sm text-gray-500">{applications.length} application(s)</p>
        </div>
      </div>

      {applications.length === 0 && (
        <div className="mt-8 rounded-lg border border-dashed border-gray-300 p-10 text-center text-sm text-gray-500">
          No applications yet. Share the drive with students to start receiving applications.
        </div>
      )}

      <div className="mt-6 space-y-4">
        {applications.map((app) => (
          <div key={app.id} className="rounded-xl border border-gray-200 bg-white p-5 shadow-sm">
            <div className="flex items-start justify-between gap-3">
              <div>
                <h3 className="text-base font-semibold text-gray-900">{app.studentName}</h3>
                <p className="mt-0.5 text-sm text-gray-500">
                  {app.studentEmail} · {app.studentDepartment || '—'} · CGPA {app.studentCgpa ?? '—'} ·{' '}
                  {app.studentGraduationYear ?? '—'} · Applied {formatDate(app.appliedAt)}
                </p>
              </div>
              <StatusBadge status={app.status} />
            </div>

            {app.coverMessage && (
              <p className="mt-3 rounded-lg bg-gray-50 px-3 py-2 text-sm text-gray-600">{app.coverMessage}</p>
            )}
            {app.resumeUrl && (
              <a
                href={app.resumeUrl}
                target="_blank"
                rel="noreferrer"
                className="mt-2 inline-block text-sm font-medium text-indigo-600 hover:text-indigo-700"
              >
                View resume
              </a>
            )}

            <div className="mt-4 flex flex-wrap items-center gap-2">
              <span className="text-sm text-gray-500">Set status:</span>
              {statusOptions.map((s) => (
                <button
                  key={s}
                  onClick={() => updateStatus(app, s)}
                  disabled={app.status === s}
                  className={`rounded-lg px-3 py-1 text-xs font-medium ${
                    app.status === s
                      ? 'bg-gray-200 text-gray-500'
                      : 'border border-gray-300 text-gray-700 hover:bg-gray-50'
                  }`}
                >
                  {s}
                </button>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
