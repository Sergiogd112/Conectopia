package models

import org.hibernate.annotations.{Tuplizer, Target}

import javax.persistence._

@Entity
@Table(name = "message")
class Message extends PersistentEntity {
  // content
  @Column(name = "content")
  var content: String = _
  // timestamp as sql Timestamp
  @Column(name = "timestamp")
  var timestamp: java.sql.Timestamp = _
  // chat_id many to one
  @ManyToOne
  @JoinColumn(name = "chat_id")
  var chat: Chat = _
  // user_id many to one
  @ManyToOne
  @JoinColumn(name = "user_id")
  var user: User = _
  // constructor
  def this(content: String, timestamp: java.sql.Timestamp, chat: Chat, user: User) {
    this()
    this.content = content
    this.timestamp = timestamp
    this.chat = chat
    this.user = user
  }
}
