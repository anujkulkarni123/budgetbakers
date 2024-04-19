<%@ page import="com.exavalu.entities.User"%>
<%@ page import="com.exavalu.entities.Menu"%>
<%@ page import="java.util.ArrayList"%>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<nav class="navbar navbar-expand-lg navbar-white fixed-top">
	<div class="container-fluid ps-4 pe-4 fw-bold">
		<ul class="navbar-nav">
			<li class="nav-item"><a class="navbar-brand" href="#"> <svg
						xmlns="http://www.w3.org/2000/svg" width="46" height="46">
				<defs>
				<linearGradient id="a" x1="54.804%" x2="54.804%" y1="100%"
							y2="5.31%"> 
				<stop offset="0%" stop-color="#F4F1FA" />
				<stop offset="100%" stop-color="#FFFEFF" /></linearGradient></defs>
				<g fill="none">
				<path fill="#21CB87"
							d="M9.813-.032h26.374C41.607-.032 46 4.362 46 9.781v26.374c0 5.42-4.394 9.813-9.813 9.813H9.813C4.393 45.968 0 41.574 0 36.155V9.78C0 4.361 4.394-.032 9.813-.032z" />
				<path fill="#FFF"
							d="M6.571 13.799c0-2.195 1.79-3.974 3.997-3.974h24.864c2.207 0 3.997 1.779 3.997 3.974v19.16c0 2.194-1.79 3.973-3.997 3.973H10.568c-2.207 0-3.997-1.778-3.997-3.973v-19.16z" />
				<path fill="url(#a)"
							d="M10.516 11.5a2.294 2.294 0 00-2.302 2.285v19.251a2.294 2.294 0 002.302 2.285h24.968a2.294 2.294 0 002.302-2.285v-19.25a2.294 2.294 0 00-2.302-2.286H10.516z"
							transform="translate(0 -.032)" />
				<path fill="#E3750C"
							d="M37.786 18.04h1.232c.68 0 1.232.457 1.232 1.02v7.817c0 .563-.551 1.02-1.232 1.02h-1.232V18.04z" />
				<path fill="#FD9F45"
							d="M25.464 22.968c0-2.722 2.175-4.928 4.857-4.928h8.067c.574 0 1.04.472 1.04 1.056v7.745c0 .583-.466 1.056-1.04 1.056H30.32c-2.682 0-4.857-2.206-4.857-4.929z" />
				<path fill="#FFF"
							d="M30.393 25.432a2.464 2.464 0 100-4.928 2.464 2.464 0 000 4.928z" /></g></svg>
					Dashboard
			</a></li>
			<%
			// retrieve all of our parameters from login servlet
			User user = (User) session.getAttribute("USER");
			ArrayList<Menu> menuList = (ArrayList<Menu>) session.getAttribute("MENULIST");
			%>

			<li class="nav-item"><ul class="navbar-nav">
					<li class="nav-item"><a class="nav-link" aria-current="page"
						href="#">Home</a></li>
					<%
					for (Menu menu : menuList) {
						if (!menu.getMenuName().equals("Settings") && !menu.getMenuName().equals("Sign Out")) {
					%>
					<li class="nav-item"><a class="nav-link" aria-current="page"
						href="<%=menu.getMenuLink()%>"><%=menu.getMenuName()%></a></li>
					<%
					}
					}
					%>
				</ul></li>
		</ul>

		<ul class="navbar-nav d-flex align-items-center">
			<li class="nav-item">
				<button type="button" class="btn btn-primary btn-sm">+
					Record</button>
			</li>
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" role="button"
				data-bs-toggle="dropdown" aria-current="page" href="#"> <img
					style="width: 46px;"
					class="img-thumbnail bg-transparent border-0 rounded-circle pt-0 pb-0"
					src="${pageContext.request.contextPath}/<%=user.getProfilePicturePath()%>"
					class="rounded-circle"> <%=user.getFirstName() + " " + user.getLastName()%>
			</a>
				<ul class="dropdown-menu">
					<%
					for (Menu menu : menuList) {
						if (menu.getMenuName().equals("Settings") || menu.getMenuName().equals("Sign Out")) {
							System.out.println(menu.getMenuName());
					%>
					<li class="nav-item"><a class="nav-link" aria-current="page"
						href="<%=menu.getMenuLink()%>"><%=menu.getMenuName()%></a></li>
					<%
					}
					}
					%>
				</ul></li>
		</ul>

	</div>
</nav>

