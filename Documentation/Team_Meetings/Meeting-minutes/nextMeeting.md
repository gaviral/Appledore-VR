1. Module-1 retrospective

	A. Focus on individual components -> seamless integration.
		a. good example: map_rendering
				- didn't care about UI
				- didn't care about code-style/data structs
				- tested with their own images
				- seamlessly included to the code
					- UI and data-structs integrated

		b. whereas: Distance, time, calories and past trips
				x did care about UI
				x did care about data-structs
				x tested with UI
				x didn't work in the end 
				x critical time spent on these functionalities

	So it is recommended we follow the following:

	a. Create functions with their specifications in team meetings.
	b. Suggest major change to anything (code-style, data-structs, function-specitions, etc) in team-meetings
	c. Limit each commit to similar type of changes
	a. Work on controller + model not view. 
	b. Follow coding standards in Github repo. (can only be changed in group meetings)
	c. Github branches: when working on a new function(s), follow these steps:
		a. Create a new branch
		b. Comments the functionalilty
		c. Write unit tests
		d. Code
		e. Pull request to merge branch into master
		f. Pull requests will be reviewed with everyone in or before the meetings
		g. Pull requests will be accepted in the meetings after careful review by peers
		h. Delete the branches

	BENEFITS:
		1. Everyone on same page
		2. Minimize time spent on debugging, integrating and maximize development time

2. Sticking with mind-palace idea?
3. Idea name: VR-One
4. Commit - 1