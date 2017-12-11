import React, { Component } from 'react'

class LoginUserForm extends Component {

  constructor () {
    super()

    this.state = {
      username: '',
      password: ''
    }
    this.handleInputChange = this.handleInputChange.bind(this)
    this.loginUserOnSubmit = this.loginUserOnSubmit.bind(this)
  }

  handleInputChange (event) {
    const value = event.target.value
    const name = event.target.name
    this.setState({
      [name]: value
    })
  }

  loginUserOnSubmit (event) {
    event.preventDefault()
    this.setState({
      username: '',
      password: ''
    })
    console.log('Logged in user')
  }

  render () {
    return (
      <nav className="navbar bg-faded rounded blue filterBarSpacing">
        <div className='alignTopAndToTheLeft'>
          <div>
            <form onSubmit={this.loginUserOnSubmit} className="loginUserForm">
              <label>Login</label>
              <input
                type="text"
                name="username"
                classID="name"
                className="form-control"
                placeholder="Username"
                value={this.state.username}
                onChange={this.handleInputChange}/>
              <input
                type="password"
                name="password"
                classID="paw"
                className="form-control"
                placeholder="Password"
                value={this.state.password}
                onChange={this.handleInputChange}/>
              <input
                type="submit"
                id="submit"
                className="form-control"
                value="Submit"/>
            </form>
          </div>
        </div>
      </nav>
    )
  }
}

export default LoginUserForm
