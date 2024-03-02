package models
import org.hibernate.annotations.{Tuplizer, Target}

import javax.persistence._

@Entity
@Table(name = "chat")
class Chat  extends PersistentEntity {
  // name
  @Column(name = "name")
  var name: String = _
  // description
  @Column(name = "description")
  var description: String = _
  // timestamp as sql Timestamp
  @Column(name = "timestamp")
  var timestamp: java.sql.Timestamp = _
  // messages one to many
  @OneToMany
  var messages: java.util.List[Message] = _

  // constructor
  def this(name: String, description: String, timestamp: java.sql.Timestamp) {
    this()
    this.name = name
    this.description = description
    this.timestamp = timestamp
  }
}
