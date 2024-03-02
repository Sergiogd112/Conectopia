package models

import org.hibernate.annotations.{Tuplizer, Target}

import javax.persistence._

@Entity
@Table(
  name = "User",
  uniqueConstraints = Array(
    new UniqueConstraint(columnNames = Array("providerId", "userId"))
  )
)
class User extends PersistentEntity {
  //username
  @Column(name = "username")
  var username: String = _
  //email
  @Column(name = "email")
  var email: String = _
  //password
  @Column(name = "password")
  var password: String = _
  // timestamp as sql Timestamp
  @Column(name = "registered_at")
  var registered_at: java.sql.Timestamp = _
  // badges one to many
  @OneToMany(mappedBy = "user")
  var badges: java.util.List[Badges] = _
  // messages one to many
  @OneToMany(mappedBy = "user")
  var messages: java.util.List[Message] = _
  // members one to many
  @OneToMany(mappedBy = "user")
  var members: java.util.List[Member] = _
  // constructor
  def this(username: String, email: String, password: String,
           registered_at: java.sql.Timestamp) {
    this()
    this.username = username
    this.email = email
    this.password = password
    this.registered_at = registered_at
  }
}