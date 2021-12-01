'use strict'

$(function () {
  $('#search').on('click', function () {
    let code = $('#code').val()
    if (code === '') {
      $('.notSearchAnimation').addClass('animate__animated animate__rubberBand')
    } else {
      $('.searchAnimation').addClass('animate__animated animate__bounceOutUp')
    }
  })

  $('.deleteItemButton').on('click', function () {
    let orderItemId = $(this).val()
    /*alert($('#orderItemId' + orderItemId))*/
    $('#orderItemId' + orderItemId).addClass(
      'animate__animated animate__bounceOutRight'
    )
  })

  $('#world1').on('click', function () {
    $('#result').html('')
    $('#aloha').addClass('animate__animated animate__zoomOutUp')
    setTimeout(function () {
      $('#result')
        .html('<h2>その世界は違ったようだ<br>別の世界を選ぶのだ</h2>')
        .css('color', 'red')
    }, 1500)
  })

  $('#world2').on('click', function () {
    $('#result').html('')
    $('#noodle').addClass('animate__animated animate__zoomOutUp')
    setTimeout(function () {
      $('#result')
        .html('<h2>正解だ！<br>元の世界へ戻るがいい</h2>')
        .css('color', 'green')
    }, 1500)
    setTimeout(function () {
      window.location.href = 'http://localhost:8080'
    }, 2500)
  })

  $('#world3').on('click', function () {
    $('#result').html('')
    $('#coffee').addClass('animate__animated animate__zoomOutUp')
    setTimeout(function () {
      $('#result')
        .html('<h2>その世界は違ったようだ<br>別の世界を選ぶのだ</h2>')
        .css('color', 'red')
    }, 1500)
  })

  $('#world4').on('click', function () {
    $('#result').html('')
    $('#curry').addClass('animate__animated animate__zoomOutUp')
    setTimeout(function () {
      $('#result')
        .html('<h2>その世界は違ったようだ<br>別の世界を選ぶのだ</h2>')
        .css('color', 'red')
    }, 1500)
  })

  $('#world5').on('click', function () {
    $('#result').html('')
    $('#piza').addClass('animate__animated animate__zoomOutUp')
    setTimeout(function () {
      $('#result')
        .html('<h2>その世界は違ったようだ<br>別の世界を選ぶのだ</h2>')
        .css('color', 'red')
    }, 1500)
  })
})
