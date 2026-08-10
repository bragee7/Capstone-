import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import api from '../../lib/api'
import Alert from '../../components/Alert'
import Spinner from '../../components/Spinner'

const emptyForm = {
  title: '',
  description: '',
  jobType: 'INTERNSHIP',
  location: '',
  stipend: '',
  salaryPackage: '',
  minimumCgpa: '',
  eligibleDepartments: '',
  requiredSkills: '',
  applicationDeadline: '',
  driveDate: '',
}

export default function DriveFormPage() {
  const { id } = useParams()
  const isEdit = Boolean(id)
  const navigate = useNavigate()
  const [form, setForm] = useState(emptyForm)
  const [loading, setLoading] = useState(isEdit)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    if (!isEdit) return
    api
      .get(`/recruiter/drives`)
      .then((res) => {
        const drive = res.data.find((d) => d.id === Number(id))
        if (!drive) {
          throw new Error('Drive not found.')
        }
        setForm({
          title: drive.title || '',
          description: drive.description || '',
          jobType: drive.jobType || 'INTERNSHIP',
          location: drive.location || '',
          stipend: drive.stipend ?? '',
          salaryPackage: drive.salaryPackage || '',
          minimumCgpa: drive.minimumCgpa ?? '',
          eligibleDepartments: drive.eligibleDepartments || '',
          requiredSkills: drive.requiredSkills || '',
          applicationDeadline: drive.applicationDeadline ? drive.applicationDeadline.slice(0, 16) : '',
          driveDate: drive.driveDate || '',
        })
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false))
  }, [id, isEdit])

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm((f) => ({ ...f, [name]: value }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setSaving(true)
    setError('')
    try {
      const payload = {
        title: form.title,
        description: form.description || null,
        jobType: form.jobType || null,
        location: form.location || null,
        stipend: form.stipend === '' ? null : Number(form.stipend),
        salaryPackage: form.salaryPackage || null,
        minimumCgpa: form.minimumCgpa === '' ? null : Number(form.minimumCgpa),
        eligibleDepartments: form.eligibleDepartments || null,
        requiredSkills: form.requiredSkills || null,
        applicationDeadline: form.applicationDeadline ? `${form.applicationDeadline}:00` : null,
        driveDate: form.driveDate || null,
      }
      if (isEdit) {
        await api.put(`/recruiter/drives/${id}`, payload)
      } else {
        await api.post('/recruiter/drives', payload)
      }
      navigate('/recruiter')
    } catch (err) {
      setError(err.message)
    } finally {
      setSaving(false)
    }
  }

  if (loading) return <Spinner />

  return (
    <div className="mx-auto max-w-2xl px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900">{isEdit ? 'Edit hiring drive' : 'Create hiring drive'}</h1>
      <p className="mt-1 text-sm text-gray-500">
        Drives are saved as drafts and only visible to students once published.
      </p>

      {error && (
        <div className="mt-4">
          <Alert type="error">{error}</Alert>
        </div>
      )}

      <form onSubmit={handleSubmit} className="mt-6 space-y-4 rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
        <div>
          <label htmlFor="title" className="mb-1 block text-sm font-medium text-gray-700">
            Title *
          </label>
          <input
            id="title"
            name="title"
            required
            value={form.title}
            onChange={handleChange}
            className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
          />
        </div>
        <div>
          <label htmlFor="description" className="mb-1 block text-sm font-medium text-gray-700">
            Description
          </label>
          <textarea
            id="description"
            name="description"
            rows={4}
            value={form.description}
            onChange={handleChange}
            className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
          />
        </div>
        <div className="grid gap-4 sm:grid-cols-2">
          <div>
            <label htmlFor="jobType" className="mb-1 block text-sm font-medium text-gray-700">
              Job type
            </label>
            <select
              id="jobType"
              name="jobType"
              value={form.jobType}
              onChange={handleChange}
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
            >
              <option value="INTERNSHIP">Internship</option>
              <option value="FULL_TIME">Full time</option>
              <option value="PART_TIME">Part time</option>
              <option value="CONTRACT">Contract</option>
            </select>
          </div>
          <div>
            <label htmlFor="location" className="mb-1 block text-sm font-medium text-gray-700">
              Location
            </label>
            <input
              id="location"
              name="location"
              value={form.location}
              onChange={handleChange}
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
            />
          </div>
        </div>
        <div className="grid gap-4 sm:grid-cols-2">
          <div>
            <label htmlFor="stipend" className="mb-1 block text-sm font-medium text-gray-700">
              Stipend (₹)
            </label>
            <input
              id="stipend"
              name="stipend"
              type="number"
              min="0"
              value={form.stipend}
              onChange={handleChange}
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
            />
          </div>
          <div>
            <label htmlFor="salaryPackage" className="mb-1 block text-sm font-medium text-gray-700">
              Salary package
            </label>
            <input
              id="salaryPackage"
              name="salaryPackage"
              value={form.salaryPackage}
              onChange={handleChange}
              placeholder="e.g. 12 LPA"
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
            />
          </div>
        </div>
        <div className="grid gap-4 sm:grid-cols-2">
          <div>
            <label htmlFor="minimumCgpa" className="mb-1 block text-sm font-medium text-gray-700">
              Minimum CGPA
            </label>
            <input
              id="minimumCgpa"
              name="minimumCgpa"
              type="number"
              min="0"
              max="10"
              step="0.1"
              value={form.minimumCgpa}
              onChange={handleChange}
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
            />
          </div>
          <div>
            <label htmlFor="driveDate" className="mb-1 block text-sm font-medium text-gray-700">
              Drive date
            </label>
            <input
              id="driveDate"
              name="driveDate"
              type="date"
              value={form.driveDate}
              onChange={handleChange}
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
            />
          </div>
        </div>
        <div>
          <label htmlFor="applicationDeadline" className="mb-1 block text-sm font-medium text-gray-700">
            Application deadline *
          </label>
          <input
            id="applicationDeadline"
            name="applicationDeadline"
            type="datetime-local"
            required
            value={form.applicationDeadline}
            onChange={handleChange}
            className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
          />
        </div>
        <div>
          <label htmlFor="eligibleDepartments" className="mb-1 block text-sm font-medium text-gray-700">
            Eligible departments (comma separated)
          </label>
          <input
            id="eligibleDepartments"
            name="eligibleDepartments"
            value={form.eligibleDepartments}
            onChange={handleChange}
            placeholder="Computer Science, Information Technology"
            className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
          />
        </div>
        <div>
          <label htmlFor="requiredSkills" className="mb-1 block text-sm font-medium text-gray-700">
            Required skills (comma separated)
          </label>
          <input
            id="requiredSkills"
            name="requiredSkills"
            value={form.requiredSkills}
            onChange={handleChange}
            placeholder="Java, Spring Boot, SQL"
            className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
          />
        </div>
        <div className="flex gap-3">
          <button
            type="submit"
            disabled={saving}
            className="rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white hover:bg-indigo-700 disabled:opacity-60"
          >
            {saving ? 'Saving...' : isEdit ? 'Save changes' : 'Create drive'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/recruiter')}
            className="rounded-lg border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  )
}
