package models

import org.hibernate.annotations.{Tuplizer,Target}
import javax.persistence._

@Entity
class Badges extends PersistentEntity {
  //name
  @Column(name = "name")
  var name: String = _
  // description
  @Column(name = "description")
  var description: String = _
  //image
  @Column(name = "image")
  var image: String = _
  // timestamp as sql Timestamp
  @Column(name = "timestamp")
  var timestamp: java.sql.Timestamp = _
  // user_id one to one
  @ManyToOne
  @JoinColumn(name = "user_id")
  var user: User = _
  // constructor
  def this(name: String, description: String, image: String,
           timestamp: java.sql.Timestamp, user: User) {
    this()
    this.name = name
    this.description = description
    this.image = image
    this.timestamp = timestamp
    this.user = user
  }
}
