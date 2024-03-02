package models


import javax.persistence._

@Entity
@Table(name = "member")
class Member extends PersistentEntity {
  // role_id

  @OneToOne
  @JoinColumn(name = "role_id")
  var role: Role = _
  // server_id
  @ManyToOne
  @JoinColumn(name = "server_id")
  var server: Server = _
  // user_id
  @ManyToOne
  @JoinColumn(name = "user_id")
  var user: User = _

  // constructor
  def this(role: Role, server: Server, user: User) {
    this()
    this.role = role
    this.server = server
    this.user = user
  }
}
