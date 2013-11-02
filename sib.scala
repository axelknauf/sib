import java.net.Socket
import java.io.PrintWriter
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Scala IRC Bot, based upon apbot[0]
 * [0] http://sourceforge.net/p/apbot/home/apbot/
 */
object Sib {

  val host = "127.0.0.1"
  val port = 6667
  val nick = "sib"
  val channel = "#test"
  val symbol = "!"
  val debugOut = true

  val s = new Socket(host, port)
  val out = new PrintWriter(s.getOutputStream(), true)
  val in = new BufferedReader(new InputStreamReader(s.getInputStream()))

  def login {
    out.println("USER " + nick + " " + nick + " " + nick + " :sib")
    out.println("NICK " + nick)
    out.println("JOIN " + channel)
  }

  def ping(msg : String) {
    println("Got ping, token is [" + msg + "]")
    out.println("PONG :" + msg)
  }

  def sendMessageToChannel(channel: String, msg: String) {
    out.println("PRIVMSG "+ channel +" :" + msg)
  }

  def doActionInChannel(channel: String, action: String) {
     out.println("PRIVMSG "+ channel +" :\u0001ACTION " + action + "\u0001")
  }

  def main(argv : Array[String]) {
    login

    do {
      var line = in.readLine();
      println(line)

      if (line.contains("PING :")) {
        ping(line.split(":")(1))
      }
      else if (line.contains("PRIVMSG")) {
        val tokens = line.split(" ")
        if (tokens.length < 4) {
          println("Got weird line (less than four tokens)..")
        } else {
          val msg = line.split(" ").drop(3).mkString(" ").drop(1)
          val cmd = msg.split(" ").head
          val args = msg.split(" ").tail
          val channel = line.split(" ")(2)
          val directMessage = channel == nick
          val user = line.drop(1).split("!")(0)

          if (debugOut) {
            println("msg = " + msg)
            println("cmd = " + cmd)
            println("args = " + args.mkString(","))
            println("channel = " + channel)
            println("directMessage = " + directMessage)
            println("user = " + user)
          }

          if (cmd == symbol + "bang") {
            sendMessageToChannel(channel, "Booooyah!")
          } else if (cmd == symbol + "boom") {
            doActionInChannel(channel, "scratches his head")
          }
        }
      } 
    } while (true)
  }

}



/*

def joinchan(channel):
  s.send("PRIVMSG "+ CHANNEL +" :Joining "+ channel +"\r\n")
  s.send("JOIN "+ channel +"\r\n")
def partchan(channel):
  s.send("PRIVMSG "+ CHANNEL +" :Leaving "+ channel +"\r\n")
  s.send("PART "+ channel +"\r\n")
def hello(user):
  s.send("PRIVMSG "+ CHANNEL +" :G'day "+ nick +"!\n")
def quitIRC():
  s.send("QUIT "+ CHANNEL +"\n")
def fail():
  s.send("PRIVMSG "+ CHANNEL +" :Either you do not have the permission to do that, or that is not a valid command.\n")
def fish(user):
  s.send("PRIVMSG "+ CHANNEL +" :\x01ACTION slaps "+ user +" with a wet sloppy tuna fish.\x01\r\n")
  time.sleep(1)
  s.send("PRIVMSG "+ CHANNEL +" :take that bitch\n")
def cake(sender):
   if food == True: 
     s.send("PRIVMSG "+ CHANNEL +" :\x01ACTION is making "+ sender +" a cake\x01\r\n")
     time.sleep(10)
     s.send("PRIVMSG "+ CHANNEL +" :\x01ACTION has finished making "+ sender +"'s cake\x01\r\n")
     time.sleep(1)
     s.send("PRIVMSG "+ CHANNEL +" :Here you go "+ sender +"! I hope you enjoy it!\r\n")
   else:
     s.send("PRIVMSG "+ CHANNEL +" :Command not loaded\r\n")
def echo(message):
  s.send("PRIVMSG "+ CHANNEL +" :"+ message +"\r\n") 
def pepsi(user):
  if food == True:
     s.send("PRIVMSG "+ CHANNEL +" :\x01ACTION dispenses a can of Pepsi for "+ user +"\x01\r\n")
  else:
     s.send("PRIVMSG "+ CHANNEL +" :Command not loaded\r\n")
def coke(user):
  if food == True:
     s.send("PRIVMSG "+ CHANNEL +" :\x01ACTION dispenses a can of Coke for "+ user +"\x01\r\n")
  else:
     s.send("PRIVMSG "+ CHANNEL +" :Command not loaded\r\n")

s = socket.socket( )
s.connect((HOST, PORT))
s.send("USER "+ NICK +" "+ NICK +" "+ NICK +" :apbot\n")
s.send("NICK "+ NICK +"\r\n")
s.send("JOIN "+ HOME_CHANNEL +"\r\n")
while 1:
  line = s.recv(2048)
  line = line.strip("\r\n")
  print line
  stoperror = line.split(" ")
  if ("PING :" in line):
        pingcmd = line.split(":", 1)
        pingmsg = pingcmd[1]
        ping(pingmsg)
  elif "PRIVMSG" in line:
      
      if len(line) < 30:
        print blank
      elif len(stoperror) < 4:
        print blank
      else:
        complete = line.split(":", 2)
        info = complete[1]
        msg = line.split(":", 2)[2] ##the thing that was said
        cmd = msg.split(" ")[0]
        CHANNEL = info.split(" ")[2] ##channel from which it was said
        user = line.split(":")[1].split("!")[0] ## the person that said the thing
        arg = msg.split(" ")
        
        if "hello " + NICK ==cmd:
          hello(user)
          print "recieved hello"
        elif "hey " + NICK ==cmd:
          hello(user)
          "print recieved hello"
        elif "hi " + NICK ==cmd:
          hello(user)
          "print recieved hello"
        elif SYMBOL + "join"==cmd and len(arg) > 1:
          x = line.split(" ", 4)
          newchannel = x[4]
          joinchan(newchannel)
        elif SYMBOL + "leave"==cmd and len(arg) > 1:
          x = line.split(" ", 4)
          newchannel = x[4]
          partchan(newchannel)
        elif SYMBOL + "quit"==cmd:
          quitIRC()
        elif SYMBOL + "coke"==cmd and len(arg) > 1:
          x = line.split(" ")
          recvr = x[4]
          coke(recvr)
        elif SYMBOL + "pepsi"==cmd and len(arg) > 1:
          x = line.split(" ")
          recvr = x[4]
          pepsi(recvr)
        elif SYMBOL + "fish"==cmd and len(arg) > 1:
          x = line.split(" ")
          recvr = x[4]
          fish(recvr)
        elif SYMBOL + "bomb"==cmd and len(arg) > 1:
          x = line.split(" ")
          recvr = x[4]
          bomb(recvr)
        elif SYMBOL + "cake"==cmd:
          cake(user)
        elif SYMBOL + "echo"==cmd:
          x = msg.split(" ", 1)[1]
          echo(x)
        
        elif line.find(""+ SYMBOL +"load") != -1:
          plugin = msg.split(" ")[1]
          load(plugin)
       
        elif line.find(""+ SYMBOL +"unload") != -1:
          plugin = msg.split(" ")[1]
          unload(plugin)
       
        elif SYMBOL in cmd:
          fail()
*/

