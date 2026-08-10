import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import api from '../../lib/api'
import Spinner from '../../components/Spinner'
import Alert from '../../components/Alert'

function formatDate(value) {
  if (!value) return '—'
  return new Date(value).toLocaleString(undefined, {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

export default function DriveDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [drive, setDrive] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [coverMessage, setCoverMessage] = useState('')
  const [resumeUrl, setResumeUrl] = useState('')
  const [applying, setApplying] = useState(false)
  const [applied, setApplied] = useState(false)
  const [feedback, setFeedback] = useState('')

  useEffect(() => {
    api
      .get(`/drives/${id}`)
      .then((res) => setDrive(res.data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false))

    api
      .get('/applications/my')
      .then((res) => {
        if (res.data.some((a) => a.driveId === Number(id))) {
          setApplied(true)
        }
      })
      .catch(() => {})
  }, [id])

  const handleApply = async (e) => {
    e.preventDefault()
    setApplying(true)
    setFeedback('')
    try {
      await api.post('/applications', {
        driveId: Number(id),
        coverMessage: coverMessage || null,
        resumeUrl: resumeUrl || null,
      })
      setApplied(true)
      setFeedback('success')
    } catch (err) {
      setFeedback('error')
      setError(err.message)
    } finally {
      setApplying(false)
    }
  }

  if (loading) return <Spinner />
  if (error) {
    return (
      <div className="mx-auto max-w-3xl px-4 py-8">
        <Alert type="error">{error}</Alert>
      </div>
    )
  }

  return (
    <div className="mx-auto max-w-3xl px-4 py-8">
      <button
        onClick={() => navigate('/student')}
        className="mb-4 text-sm font-medium text-indigo-600 hover:text-indigo-700"
      >
        &larr; Back to drives
      </button>

      <div className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
        <div className="flex items-start justify-between gap-3">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">{drive.title}</h1>
            <p className="mt-1 text-gray-500">
              {drive.companyName}
              {drive.location ? ` · ${drive.location}` : ''}
            </p>
          </div>
          <span className="rounded-md bg-indigo-50 px-2 py-1 text-xs font-medium text-indigo-700">
            {drive.jobType}
          </span>
        </div>

        <dl className="mt-6 grid grid-cols-2 gap-4 text-sm sm:grid-cols-4">
          <div>
            <dt className="text-xs text-gray-500">Stipend / Package</dt>
            <dd className="font-medium text-gray-900">
              {drive.stipend ? `₹${drive.stipend.toLocaleString()}` : drive.salaryPackage || '—'}
            </dd>
          </div>
          <div>
            <dt className="text-xs text-gray-500">Min CGPA</dt>
            <dd className="font-medium text-gray-900">{drive.minimumCgpa ?? '—'}</dd>
          </div>
          <div>
            <dt className="text-xs text-gray-500">Drive date</dt>
            <dd className="font-medium text-gray-900">
              {drive.driveDate ? new Date(drive.driveDate).toLocaleDateString() : '—'}
            </dd>
          </div>
          <div>
            <dt className="text-xs text-gray-500">Deadline</dt>
            <dd className="font-medium text-gray-900">{formatDate(drive.applicationDeadline)}</dd>
          </div>
        </dl>

        {drive.description && (
          <div className="mt-6">
            <h2 className="text-sm font-semibold text-gray-900">About the role</h2>
            <p className="mt-1 text-sm whitespace-pre-line text-gray-600">{drive.description}</p>
          </div>
        )}

        <div className="mt-6 grid gap-4 text-sm sm:grid-cols-2">
          {drive.eligibleDepartments && (
            <div>
              <h2 className="text-sm font-semibold text-gray-900">Eligible departments</h2>
              <p className="mt-1 text-gray-600">{drive.eligibleDepartments}</p>
            </div>
          )}
          {drive.requiredSkills && (
            <div>
              <h2 className="text-sm font-semibold text-gray-900">Required skills</h2>
              <p className="mt-1 text-gray-600">{drive.requiredSkills}</p>
            </div>
          )}
        </div>
      </div>

      <div className="mt-6 rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
        {applied ? (
          <Alert type="success">You have already applied to this drive. Track it under My Applications.</Alert>
        ) : (
          <form onSubmit={handleApply} className="space-y-4">
            <h2 className="text-base font-semibold text-gray-900">Apply now</h2>
            {feedback === 'error' && <Alert type="error">{error}</Alert>}
            <div>
              <label htmlFor="coverMessage" className="mb-1 block text-sm font-medium text-gray-700">
                Cover message <span className="text-gray-400">(optional)</span>
              </label>
              <textarea
                id="coverMessage"
                rows={3}
                value={coverMessage}
                onChange={(e) => setCoverMessage(e.target.value)}
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
              />
            </div>
            <div>
              <label htmlFor="resumeUrl" className="mb-1 block text-sm font-medium text-gray-700">
                Resume URL <span className="text-gray-400">(optional)</span>
              </label>
              <input
                id="resumeUrl"
                type="url"
                value={resumeUrl}
                onChange={(e) => setResumeUrl(e.target.value)}
                placeholder="https://example.com/resume.pdf"
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
              />
            </div>
            <button
              type="submit"
              disabled={applying}
              className="rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white hover:bg-indigo-700 disabled:opacity-60"
            >
              {applying ? 'Submitting...' : 'Submit application'}
            </button>
          </form>
        )}
      </div>
    </div>
  )
}
