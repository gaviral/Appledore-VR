void delay(unsigned int seconds);
void change_pallette(int pallette_num);
void clear_screen(void);
void render_button(struct Buttons button, int render);
void render_screen(int screen_num, int render);
bool button_pressed(struct Buttons button, Point touch_point);
void any_button_pressed(Point touch_point);
void change_button_color(struct Buttons *button, int new_color);
void controller_init(void);
void init_buttons(void);


// TODO: REMOVE
void circle_helper(int x0, int y0);
void render_circle(int i);
