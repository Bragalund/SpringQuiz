import React, { Component } from 'react'

class CreateUserForm extends Component {

  constructor () {
    super()
    this.state = {
      username: '',
      firstname: '',
      lastname: '',
      email: '',
      password: '',
      repassword: ''
    }
    this.handleInputChange = this.handleInputChange.bind(this)
    this.createUserOnSubmit = this.createUserOnSubmit.bind(this)
  }

  sendPost () {
    return fetch('/users', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: this.state.username,
        firstname: this.state.firstname,
        lastname: this.state.lastname,
        email: this.state.email,
        password: this.state.password,
        repassword: this.state.repassword
      })
    }).then(response => {
      if (response.ok) {
        response.json()
      } else {
        console.log('There was an error: ' + response)
      }
    })
      .catch(err => console.log(err))
  }

  handleInputChange (event) {
    const value = event.target.value
    const name = event.target.name
    this.setState({
      [name]: value
    })
  }

  createUserOnSubmit (event) {
    event.preventDefault()
    this.setState({
      username: '',
      firstname: '',
      lastname: '',
      password: '',
      repassword: ''
    })
    const response = this.sendPost()
      .then(user => {
        console.log(user)
      })
  }

  render () {
    return (
      <nav className="navbar bg-faded rounded orange filterBarSpacing">
        <div className='alignTopAndToTheLeft'>
          <form onSubmit={this.createUserOnSubmit} className="createUserForm">
            <label>Register new user</label>
            <input
              type="text"
              name="username"
              classID="username"
              className="form-control"
              placeholder="Username"
              value={this.state.username}
              onChange={this.handleInputChange}/>
            <input
              type="text"
              name="firstname"
              classID="firstname"
              className="form-control"
              placeholder="First name"
              value={this.state.firstname}
              onChange={this.handleInputChange}/>
            <input
              type="text"
              name="lastname"
              classID="lastname"
              className="form-control"
              placeholder="Last name"
              value={this.state.lastname}
              onChange={this.handleInputChange}/>
            <input
              type="text"
              name="email"
              classID="email"
              className="form-control"
              placeholder="Email"
              value={this.state.email}
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
              type="password"
              name="repassword"
              classID="repaw"
              className="form-control"
              placeholder="Re-enter password"
              value={this.state.repassword}
              onChange={this.handleInputChange}/>
            <input
              type="submit"
              classID="submit"
              className="form-control"
              value="Submit"/>
          </form>
        </div>
      </nav>
    )
  }

}

export default CreateUserForm
