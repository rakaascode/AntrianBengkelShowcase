package dev.inteiintel.teduhserviceapp.presentation.auth

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.OnBoardingModel
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen
import dev.inteiintel.teduhserviceapp.ui.theme.DarkOrange
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.MidnightBlue
import kotlinx.coroutines.delay

@Composable
fun AuthLoginScreen(viewModel: AuthViewModel= hiltViewModel (), navController: NavController){
    val context = LocalContext.current
    val loadingState = viewModel.loading.collectAsState()

    val pagerState = rememberPagerState(pageCount = {3})

    val onBoardingContentState by  viewModel.getDataOnBoardingModel.collectAsState()

    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000)
            val nextPage = if (pagerState.currentPage + 1 < pagerState.pageCount) {
                pagerState.currentPage + 1
            } else {
                0
            }
            pagerState.animateScrollToPage(nextPage)
        }

    }

    LaunchedEffect(Unit) {
        viewModel.getCurrentRefreshToken(context).collect { token ->
            if (token.isNotEmpty()) {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Auth.route) {
                        inclusive = true
                    }
                }
            }
        }
    }


    Box(Modifier.fillMaxSize()){
        Column (Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
            Column (Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f),){

                HorizontalPager(pagerState){ pager ->
                    Box(Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                    ){
                        Image(painterResource(R.drawable.ic_ob), contentDescription = null, contentScale = ContentScale.FillBounds)
                        OnBoardingContent(pagerState.currentPage,onBoardingContentState)
                    }
                }

            }

            Spacer(Modifier.height(8.dp))

            PagerIndicator(pagerState.pageCount,pagerState.currentPage)

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {
                        viewModel.onLoginClick (context){ message ->
                            Toast.makeText(context,message, Toast.LENGTH_LONG).show()
                        }
                    },
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 1.dp ),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xFFC6C5D4)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(painter = painterResource(R.drawable.ic_google), contentDescription = null,tint = Color.Unspecified, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(30.dp))
                    Text("Masuk dengan Google", color = DarkSlate, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                val teks = "Dengan melanjutkan, Anda menyetujui\nSyarat & Ketentuan kami".uppercase()
                Spacer(Modifier.height(8.dp))
                Text(teks, fontSize = 12.sp, color = DimGray, fontWeight = FontWeight.Normal, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
            }

        }
        if (loadingState.value){
            Box(Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }

    }

}


@Composable
fun OnBoardingContent(currentPageIndex: Int, contentOnBoardingModel: List<OnBoardingModel>)
{
    val item =contentOnBoardingModel[currentPageIndex]

    Column (Modifier.fillMaxWidth().fillMaxHeight(), verticalArrangement = Arrangement.Center){

        Image(
            painter = painterResource(item.Image),
            modifier = Modifier.aspectRatio(1f),
            contentDescription = null
        )
        Text(
            text = stringResource(item.titleSatu),
            color = MidnightBlue,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(item.titleDua),
            color = DarkOrange,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(item.subtitle),
            color = DarkSlate,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }

}


@Composable
fun PagerIndicator(
    pageCount: Int,
    currentPageIndex: Int,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
            ,
            horizontalArrangement = Arrangement.Center
        ) {

            repeat(pageCount) { iteration ->

                val isSelected = currentPageIndex == iteration

                val color by animateColorAsState(
                    targetValue = if (isSelected) MidnightBlue else DimGray,
                    animationSpec = tween(300),
                    label = "dotColor"
                )

                val width by animateDpAsState(
                    targetValue = if (isSelected) 35.dp else 8.dp,
                    animationSpec = tween(300),
                    label = "dotWidth"
                )

                Box(
                    modifier = modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .height(8.dp)
                        .width(width)
                )
            }
        }
    }
}