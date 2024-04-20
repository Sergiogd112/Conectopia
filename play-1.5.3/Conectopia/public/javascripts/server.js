function fuzzyFindUser() {
    const search = document.getElementById('searchUser').value;
    const url = '/search/user?query=' + search+'&excludeServer='+document.getElementById('serverName').textContent;
    fetch(url)
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            var result = '';
            console.log(data);
            for (const dataKey in data) {
                result += '<button type="button" class="list-group-item list-group-item-action" onclick="sendInvite('+dataKey+',\''+
                    data[dataKey]+'\')">'
                    + data[dataKey] + '</button>';
            }
            document.getElementById('searchResults').innerHTML = result;
        });
}

function sendInvite(userid,username) {
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
