const url ='http://localhost:8080/info'


fetch(url)
    .then(res => { res.json().then(
        user=>{
            let navBarUser = ""
            navBarUser += "<b class=\"text-white\">"+user.email+"</b>"
            navBarUser += "<span class=\"text-white\"> with roles: </span>"
            navBarUser += "<span class=\"text-white\">"
            user.roles.forEach((role) => navBarUser += role.role.replace('ROLE_','')+' ')
            navBarUser += "</span>"
            document.getElementById("navBarUser").innerHTML = navBarUser

            let tableUser = ""
            tableUser += "<tr>"
            tableUser += "<td>"+user.id+"</td>"
            tableUser += "<td>"+user.firstName+"</td>"
            tableUser += "<td>"+user.lastName+"</td>"
            tableUser += "<td>"+user.age+"</td>"
            tableUser += "<td>"+user.email+"</td>"
            tableUser += "<td>"
            user.roles.forEach((role) => tableUser += role.role.replace('ROLE_','')+" ")
            tableUser += "</td>"
            document.getElementById("userInfo").innerHTML = tableUser
        }
    )
    })

