package models
import org.hibernate.annotations.{Tuplizer, Target}

import javax.persistence._
@Entity
@Table(name = "server")
class Server  extends PersistentEntity {
  // name
  @Column(name = "name")
  var name: String = _
  // description
  @Column(name = "description")
  var description: String = _
  // timestamp as sql Timestamp
  @Column(name = "timestamp")
  var timestamp: java.sql.Timestamp = _
  // roles one to many
  @OneToMany
  var roles: java.util.List[Role] = _
  // chats one to many
  @OneToMany
  var chats: java.util.List[Chat] = _
  //members one to many
  @OneToMany
  var members: java.util.List[Member] = _
  // constructor
  def this(name: String, description: String, timestamp: java.sql.Timestamp) {
    this()
    this.name = name
    this.description = description
    this.timestamp = timestamp
  }
}
