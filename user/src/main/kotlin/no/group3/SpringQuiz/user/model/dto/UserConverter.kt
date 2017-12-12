package no.group3.SpringQuiz.user.model.dto

import no.group3.SpringQuiz.user.model.entity.User

class UserConverter{
    companion object {
        fun transform(entity: User): UserDto {
            return UserDto(
                    id = entity.userId,
                    userName = entity.userName,
                    firstName = entity.firstName,
                    lastName = entity.lastName,
                    email = entity.email
            )
        }

        fun transform(entities: Iterable<User>): List<UserDto> {
            return entities.map { transform(it) }
        }
    }
}

