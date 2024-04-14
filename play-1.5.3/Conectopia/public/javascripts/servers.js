function createForm() {
    if (document.getElementById("createServerForm").style.display == "none") {
        document.getElementById("createServerForm").style.display = "block";
        document.getElementById("createButton").innerText = "Cancel";
    }else{
        document.getElementById("createServerForm").style.display = "none";
        document.getElementById("createButton").innerText = "Create Server";
    }
}