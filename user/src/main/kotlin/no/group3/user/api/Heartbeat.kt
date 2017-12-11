package no.group3.user.api

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Heartbeat{

    @RequestMapping("/heartbeat")
    fun heartbeat(): String{
        return "<3"
    }

}