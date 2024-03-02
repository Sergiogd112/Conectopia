package models

import org.hibernate.annotations.{Tuplizer, Target}

import javax.persistence._

@Entity
@Table(name = "role")
class Role extends PersistentEntity {
  // name
  @Column(name = "name")
  var name: String = _
  // color in hex
  @Column(name = "color")
  var color: String = _
  // members one to one
  @OneToOne
  var members: java.util.List[User] = _
  // servers many to one
  @ManyToOne
  @JoinColumn(name = "server_id")
  var server: Server = _
  // chat many to one
  @ManyToOne
  @JoinColumn(name = "chat_id")
  var chat: Chat = _

  // constructor
  def this(name: String, color: String, members: java.util.List[User], server: Server, chat: Chat) {
    this()
    this.name = name
    this.color = color
    this.members = members
    this.server = server
    this.chat = chat
  }
}
