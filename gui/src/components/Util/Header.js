import React, {Component} from 'react'
import { Link } from 'react-router-dom' // eslint-disable-line no-unused-vars

class Header extends Component {
  render () {
    return (
      <div className="jumbotron tourquise">
        <a href="/">
          <h1>Wishlist</h1>
          <p className="lead">The wishlist-app</p>
        </a>
        <p className="text-center">
          <Link to={'/user'} className="btn btn-primary-lg" role="button">Login/Register</Link>
          <Link to={'/wishes'} className="btn btn-primary-lg" role="button">Your wishes</Link>
        </p>
      </div>
    )
  }
}

export default Header
