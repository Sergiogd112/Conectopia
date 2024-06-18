function fuzzyFindUser() {
    const search = document.getElementById('searchUser').value;
    const url = '/search/user?query=' + search + '&excludeServer=' + document.getElementById('serverName').textContent;
    fetch(url)
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            var result = '';
            console.log(data);
            for (const dataKey in data) {
                result += '<button type="button" class="list-group-item list-group-item-action" onclick="sendInvite(' + dataKey + ',\'' + data[dataKey] + '\')">' + data[dataKey] + '</button>';
            }
            document.getElementById('searchResults').innerHTML = result;
        });
}

function fuzzyFindMember() {
    const search = document.getElementById('searchMember').value;
    const url = '/search/member?query=' + search + '&ServerName=' + document.getElementById('serverName').textContent;
    fetch(url)
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            var result = '';
            console.log(data);
            for (const dataKey in data) {
                result += '<div class="list-group list-group-item"> <span>' + data[dataKey] +
                    '</span> <button type="button" class="btn btn-danger" onclick="removeUser(' +
                    dataKey + ',\'' + data[dataKey] + '\')">Remove</button></div>';
            }
            document.getElementById('searchMemberResults').innerHTML = result;
        });
}

function sendInvite(userid, username) {
    // pop up to confirm invite
    const confirm = window.confirm('Are you sure you want to invite ' + username + ' to this server?');
    if (!confirm) {
        return;
    }
    // use post request to send invite
    const url = '/dashboard/invite';
    const data = "userid=" + userid + "&servername=" + document.getElementById('serverName').textContent;
    fetch(url, {
        method: 'POST', headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }, body: data

    })
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            console.log(data);
        });

}

function removeUser(userid, username) {
    // pop up to confirm invite
    const confirm = window.confirm('Are you sure you want to remove ' + username + ' from this server?');
    if (!confirm) {
        return;
    }
    // use post request to send invite
    const url = '/dashboard/server/' + document.getElementById('serverName').textContent + '/user/' + userid;
    fetch(url, {
        method: 'DELETE', headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            console.log(data);
        });

}
function createForm() {
    if (document.getElementById("createServerForm").style.display == "none") {
        document.getElementById("createServerForm").style.display = "block";
        document.getElementById("createButton").innerText = "Cancel";
    }else{
        document.getElementById("createServerForm").style.display = "none";
        document.getElementById("createButton").innerText = "Create Server";
    }
}
