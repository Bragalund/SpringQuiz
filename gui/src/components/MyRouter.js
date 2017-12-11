import React from 'react'
import {Route, Switch} from 'react-router-dom'
import Home from './Home/Home'
import User from './User/User'

const MyRouter = () => (
    <main>
        <Switch>
            <Route exact path='/' component={Home}/>
            <Route path='/user' component={User}/>
        </Switch>
    </main>
)

export default MyRouter
