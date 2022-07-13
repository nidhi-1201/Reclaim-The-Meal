function validate(){
 var pwd = document.getElementById("exampleFormControlInput4").value;
 if(pwd.length<8){
   document.getElementById("error").innerHTML = "Password must contain atleast 8 charcters";
   return false;
 }
 else{
   pwd.innerHTML="";
   return true;
 }
}