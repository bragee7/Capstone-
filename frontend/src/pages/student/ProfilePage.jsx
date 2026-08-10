import { useEffect, useState } from 'react'
import api from '../../lib/api'
import Spinner from '../../components/Spinner'
import Alert from '../../components/Alert'

const fields = [
  { key: 'registrationNumber', label: 'Registration number', type: 'text' },
  { key: 'department', label: 'Department', type: 'text' },
  { key: 'college', label: 'College', type: 'text' },
  { key: 'graduationYear', label: 'Graduation year', type: 'number' },
  { key: 'cgpa', label: 'CGPA', type: 'number', step: '0.01' },
  { key: 'skills', label: 'Skills', type: 'text' },
  { key: 'resumeUrl', label: 'Resume URL', type: 'url' },
]

export default function ProfilePage() {
  const [profile, setProfile] = useState(null)
  const [form, setForm] = useState(null)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [saved, setSaved] = useState(false)

  useEffect(() => {
    api
      .get('/students/me')
      .then((res) => {
        setProfile(res.data)
        setForm({
          registrationNumber: res.data.registrationNumber || '',
          department: res.data.department || '',
          college: res.data.college || '',
          graduationYear: res.data.graduationYear || '',
          cgpa: res.data.cgpa ?? '',
          skills: res.data.skills || '',
          resumeUrl: res.data.resumeUrl || '',
          bio: res.data.bio || '',
        })
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false))
  }, [])

  const handleChange = (e) => {
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setSaving(true)
    setSaved(false)
    setError('')
    try {
      const payload = {}
      for (const [key, value] of Object.entries(form)) {
        payload[key] = key === 'graduationYear' || key === 'cgpa' ? (value === '' ? null : Number(value)) : value
      }
      const res = await api.put('/students/me', payload)
      setProfile(res.data)
      setSaved(true)
    } catch (err) {
      setError(err.message)
    } finally {
      setSaving(false)
    }
  }

  if (loading) return <Spinner />
  if (error) return (
    <div className="mx-auto max-w-2xl px-4 py-8">
      <Alert type="error">{error}</Alert>
    </div>
  )

  return (
    <div className="mx-auto max-w-2xl px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900">Student profile</h1>

      <div className="mt-4 rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
        <div className="flex items-center justify-between">
          <p className="text-sm font-medium text-gray-700">Profile completion</p>
          <p className="text-sm font-semibold text-indigo-600">{profile.profileCompletion}%</p>
        </div>
        <div className="mt-2 h-2 w-full overflow-hidden rounded-full bg-gray-100">
          <div
            className="h-full rounded-full bg-indigo-600 transition-all"
            style={{ width: `${profile.profileCompletion}%` }}
          />
        </div>
      </div>

      {saved && (
        <div className="mt-4">
          <Alert type="success">Profile saved successfully.</Alert>
        </div>
      )}
      {error && (
        <div className="mt-4">
          <Alert type="error">{error}</Alert>
        </div>
      )}

      <form onSubmit={handleSubmit} className="mt-6 space-y-4 rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
        {fields.map((field) => (
          <div key={field.key}>
            <label htmlFor={field.key} className="mb-1 block text-sm font-medium text-gray-700">
              {field.label}
            </label>
            <input
              id={field.key}
              name={field.key}
              type={field.type}
              step={field.step}
              value={form[field.key]}
              onChange={handleChange}
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
            />
          </div>
        ))}
        <div>
          <label htmlFor="bio" className="mb-1 block text-sm font-medium text-gray-700">
            Bio
          </label>
          <textarea
            id="bio"
            name="bio"
            rows={3}
            value={form.bio}
            onChange={handleChange}
            className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
          />
        </div>
        <button
          type="submit"
          disabled={saving}
          className="rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white hover:bg-indigo-700 disabled:opacity-60"
        >
          {saving ? 'Saving...' : 'Save profile'}
        </button>
      </form>
    </div>
  )
}
