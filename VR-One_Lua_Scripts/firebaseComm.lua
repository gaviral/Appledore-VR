function http_patch(user_id, content, length)
	-- Clear the wtachdog times and Download a file
	tmr.wdclr()

	-- run garbage collecton
	collectgarbage()
	
	--local post_line = string.format("POST /Users/%s/Controller.json HTTP/1.1\r\n", user_id)
	local patch_line = string.format("PATCH /Users/%s/Controller.json HTTP/1.1\r\n", user_id)
	local content_length = string.format("Content-Length: %s\r\n", length)
  
	patch_s = patch_line
			.."Host: vr-one-4e3bb.firebaseio.com\r\n"
			.."Connection: close\r\n"
			.."Accept-Charset: utf-8\r\n"
			.."Accept-Encoding: \r\n"
			.."User-Agent: Mozilla/4.0 (compatible; esp8266 Lua; Windows NT 5.1)\r\n"
			.."Accept: application/json, text/plain, */*\r\n"
			..content_length
			.."Content-Type: application/json\r\n\r\n"
			..content


    conn = nil
    conn = tls.createConnection(tls.TCP, 0)
	--print("Trying PATCH")
    conn:on("receive", function(conn,payload) 
						--print(payload)
						collectgarbage()
						payload = nil
						end)
	
	conn:on("disconnection", function(conn) 
        conn = nil
		--print("Disconnected");
    end)
	
    
    conn:on("connection", function(conn)
				--print("Connected") 
				conn:send(patch_s) 
				end)
	
	--[[conn:on("sent", function(conn)
				conn:send(patch_s)
				end)]]
	
	conn:connect(443, "vr-one-4e3bb.firebaseio.com")
	

	--tidy up after request
	collectgarbage()

end