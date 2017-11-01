package no.group3.user.model.dto

import no.group3.user.model.entity.User

class UserConverter{
    companion object {
        fun transform(user: User): UserDto{
            return UserDto(
                    id =user.userId?.toString(),
                    userName = user.userName,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = user.email
            )
        }
        fun transform(entities: Iterable<User>): List<UserDto> {
            return entities.map { transform(it) }
        }
    }
}
