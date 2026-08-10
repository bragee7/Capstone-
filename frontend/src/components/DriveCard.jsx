import { Link } from 'react-router-dom'

function formatDate(value) {
  if (!value) return '—'
  return new Date(value).toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' })
}

export default function DriveCard({ drive }) {
  return (
    <Link
      to={`/student/drives/${drive.id}`}
      className="block rounded-xl border border-gray-200 bg-white p-5 shadow-sm transition hover:border-indigo-300 hover:shadow"
    >
      <div className="flex items-start justify-between gap-3">
        <div>
          <h3 className="text-base font-semibold text-gray-900">{drive.title}</h3>
          <p className="mt-0.5 text-sm text-gray-500">{drive.companyName}</p>
        </div>
        <span className="rounded-md bg-indigo-50 px-2 py-0.5 text-xs font-medium text-indigo-700">
          {drive.jobType}
        </span>
      </div>

      <dl className="mt-4 grid grid-cols-2 gap-x-4 gap-y-2 text-sm">
        <div>
          <dt className="text-xs text-gray-500">Location</dt>
          <dd className="font-medium text-gray-700">{drive.location || '—'}</dd>
        </div>
        <div>
          <dt className="text-xs text-gray-500">Stipend / Package</dt>
          <dd className="font-medium text-gray-700">
            {drive.stipend ? `₹${drive.stipend.toLocaleString()}` : drive.salaryPackage || '—'}
          </dd>
        </div>
        <div>
          <dt className="text-xs text-gray-500">Min CGPA</dt>
          <dd className="font-medium text-gray-700">{drive.minimumCgpa ?? '—'}</dd>
        </div>
        <div>
          <dt className="text-xs text-gray-500">Deadline</dt>
          <dd className="font-medium text-gray-700">{formatDate(drive.applicationDeadline)}</dd>
        </div>
      </dl>
    </Link>
  )
}
