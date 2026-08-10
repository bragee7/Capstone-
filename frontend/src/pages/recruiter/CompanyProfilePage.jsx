import { useEffect, useState } from 'react'
import api from '../../lib/api'
import Spinner from '../../components/Spinner'
import Alert from '../../components/Alert'

const fields = [
  { key: 'companyName', label: 'Company name', type: 'text' },
  { key: 'industry', label: 'Industry', type: 'text' },
  { key: 'website', label: 'Website', type: 'url' },
  { key: 'location', label: 'Location', type: 'text' },
]

export default function CompanyProfilePage() {
  const [profile, setProfile] = useState(null)
  const [form, setForm] = useState(null)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [saved, setSaved] = useState(false)

  useEffect(() => {
    api
      .get('/companies/me')
      .then((res) => {
        setProfile(res.data)
        setForm({
          companyName: res.data.companyName || '',
          industry: res.data.industry || '',
          website: res.data.website || '',
          location: res.data.location || '',
          description: res.data.description || '',
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
      const res = await api.put('/companies/me', form)
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
      <h1 className="text-2xl font-bold text-gray-900">Company profile</h1>
      <p className="mt-1 text-sm text-gray-500">This profile is shown to students alongside your drives.</p>

      {profile?.verified && (
        <div className="mt-4">
          <Alert type="success">Your company is verified.</Alert>
        </div>
      )}
      {saved && (
        <div className="mt-4">
          <Alert type="success">Company profile saved.</Alert>
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
              value={form[field.key]}
              onChange={handleChange}
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none focus:ring-1 focus:ring-indigo-500"
            />
          </div>
        ))}
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
        <button
          type="submit"
          disabled={saving}
          className="rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white hover:bg-indigo-700 disabled:opacity-60"
        >
          {saving ? 'Saving...' : 'Save company'}
        </button>
      </form>
    </div>
  )
}
