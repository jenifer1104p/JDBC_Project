function confirmDelete() {

    return confirm(
        "Are you sure you want to delete this student?"
    );
}


function viewStudents() {

    fetch("student")

        .then(response => response.text())

        .then(data => {

            document.getElementById(
                "studentTable"
            ).innerHTML = data;

        })

        .catch(error => {

            console.log(error);

            document.getElementById(
                "studentTable"
            ).innerHTML =
                "<p>Error loading students</p>";

        });
}