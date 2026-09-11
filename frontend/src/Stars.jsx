import './ratings.css'

// Draws 5 stars: gold for the rating, grey for the rest.
// Example: value 3.6 shows 4 gold stars and 1 grey star.
const STAR = '\u2605'

export default function Stars({ value, size = 18 }) {
  const full = Math.round(value || 0)

  return (
    <span className="stars" style={{ fontSize: size }} title={`${value} out of 5`}>
      {[1, 2, 3, 4, 5].map(n => (
        <span key={n} className={n <= full ? 'star-on' : 'star-off'}>{STAR}</span>
      ))}
    </span>
  )
}