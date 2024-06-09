import play.test.*;
import play.jobs.*;
import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        // Load default data if the database is empty
        if (User.count() == 0) {
            // Usuarios
            User sergio = new User();
            sergio.username = "sergio";
            sergio.email = "sergio@mail.com";
            sergio.password = "sergio";
            sergio.save();
            User juan = new User();
            juan.username = "juan";
            juan.email = "juan@mail.com";
            juan.password = "juan";
            juan.save();
            User bryan = new User();
            bryan.username = "bryan";
            bryan.email = "bryan@mail.com";
            bryan.password = "bryan";
            bryan.save();
            // Servidores
            Server tierra = new Server();
            tierra.name = "Tierra";
            tierra.description = "El planeta tierra";
            tierra.save();
            Server marte = new Server();
            marte.name = "Marte";
            marte.description = "El planeta marte";
            marte.save();
            // Mienbros y roles
            Member sergioTierra = new Member();
            sergioTierra.user = sergio;
            sergioTierra.server = tierra;
            Role ownerTierra = new Role();
            ownerTierra.name = "Owner";
            ownerTierra.color = "magenta";
            ownerTierra.server = tierra;
            sergioTierra.save();
            ownerTierra.members.add(sergioTierra);
            sergioTierra.role = ownerTierra;
            ownerTierra.save();
            sergioTierra.save();
            sergio.members.add(sergioTierra);
            sergio.save();
            tierra.members.add(sergioTierra);
            tierra.roles.add(ownerTierra);
            tierra.save();

            Member bryanMarte = new Member();
            bryanMarte.user = bryan;
            bryanMarte.server = marte;
            bryanMarte.save();
            Role ownerMarte = new Role();
            ownerMarte.name = "Owner";
            ownerMarte.color = "magenta";
            ownerMarte.server = marte;
            ownerMarte.members.add(bryanMarte);
            bryanMarte.role = ownerMarte;
            ownerMarte.save();
            bryanMarte.save();
            bryan.members.add(bryanMarte);
            bryan.save();
            marte.members.add(bryanMarte);
            marte.roles.add(ownerMarte);
            marte.save();

            Member juanTierra = new Member();
            juanTierra.user = juan;
            juanTierra.server = tierra;
            juanTierra.save();

            Role tierraMember = new Role();
            tierraMember.name = "Member";
            tierraMember.color = "blue";
            tierraMember.server = tierra;
            tierraMember.members.add(juanTierra);
            tierraMember.save();
            juanTierra.role = tierraMember;
            juanTierra.save();
            juan.members.add(juanTierra);
            juan.save();
            tierra.members.add(juanTierra);
            tierra.roles.add(tierraMember);
            tierra.save();

            Member juanMarte = new Member();
            juanMarte.user = juan;
            juanMarte.server = marte;
            juanMarte.save();

            Role marteMember = new Role();
            marteMember.name = "Member";
            marteMember.color = "blue";
            marteMember.server = marte;
            marteMember.members.add(juanMarte);
            marteMember.save();
            juanMarte.role = marteMember;
            juanMarte.save();
            juan.members.add(juanMarte);
            juan.save();
            marte.members.add(juanMarte);
            marte.roles.add(marteMember);
            marte.save();

            // Chats
            Chat tierra_Chat_lagos = new Chat();
            tierra_Chat_lagos.name = "Lagos";
            tierra_Chat_lagos.server = tierra;
            tierra_Chat_lagos.save();
            tierra.chats.add(tierra_Chat_lagos);
            tierra.save();

            Chat marte_Chat_crateres = new Chat();
            marte_Chat_crateres.name = "Cráteres";
            marte_Chat_crateres.server = marte;
            marte_Chat_crateres.save();
            marte.chats.add(marte_Chat_crateres);
            marte.save();

            // Mensajes
            /// Tierra
            //// Lagos

            Message sergioTierraLagos = new Message();
            sergioTierraLagos.user = sergio;
            sergioTierraLagos.chat = tierra_Chat_lagos;
            sergioTierraLagos.content = "Hola, chapulín";
            sergioTierraLagos.save();
            tierra_Chat_lagos.messages.add(sergioTierraLagos);
            tierra_Chat_lagos.save();
            sergio.messages.add(sergioTierraLagos);
            sergio.save();

            Message juanTierraLagos = new Message();
            juanTierraLagos.user = juan;
            juanTierraLagos.chat = tierra_Chat_lagos;
            juanTierraLagos.content = "Buenas, chapulín";
            juanTierraLagos.save();
            tierra_Chat_lagos.messages.add(juanTierraLagos);
            tierra_Chat_lagos.save();
            juan.messages.add(juanTierraLagos);
            juan.save();

            /// Marte
            //// Cráteres

            Message bryanMarteCrateres = new Message();
            bryanMarteCrateres.user = bryan;
            bryanMarteCrateres.chat = marte_Chat_crateres;
            bryanMarteCrateres.content = "Hola, chapulón";
            bryanMarteCrateres.save();
            marte_Chat_crateres.messages.add(bryanMarteCrateres);
            marte_Chat_crateres.save();
            bryan.messages.add(bryanMarteCrateres);
            bryan.save();

            Message juanMarteCrateres = new Message();
            juanMarteCrateres.user = juan;
            juanMarteCrateres.chat = marte_Chat_crateres;
            juanMarteCrateres.content = "Buenas, chapulón";
            juanMarteCrateres.save();
            marte_Chat_crateres.messages.add(juanMarteCrateres);
            marte_Chat_crateres.save();
            juan.messages.add(juanMarteCrateres);
            juan.save();

            // Badges
            Badge badge1 = new Badge();
            badge1.name = "Badge 1";
            badge1.description = "Badge 1 description";
            badge1.image = "https://api.dicebear.com/8.x/rings/svg?seed=badge1";
            badge1.save();

            Badge badge2 = new Badge();
            badge2.name = "Badge 2";
            badge2.description = "Badge 2 description";
            badge2.image = "https://api.dicebear.com/8.x/rings/svg?seed=badge2";
            badge2.save();

            Badge badge3 = new Badge();
            badge3.name = "Badge 3";
            badge3.description = "Badge 3 description";
            badge3.image = "https://api.dicebear.com/8.x/rings/svg?seed=badge3";
            badge3.save();

            sergio.badges.add(badge1);
            sergio.badges.add(badge2);
            bryan.badges.add(badge3);
            sergio.save();
            bryan.save();
            badge1.user = sergio;
            badge1.save();
            badge2.user = sergio;
            badge2.save();
            badge3.user = bryan;
            badge3.save();

        }
    }

}